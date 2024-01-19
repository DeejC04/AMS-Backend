import React, {
  useEffect,
  useState,
} from "react";

import {TimeConfig, areTimeConfigsEqual, BoundError} from "./ThresholdBox";
import DateSlider from "./DateSlider";

// styles
import axios from "axios";
import {Button, Checkbox} from "@instructure/ui";

interface SpecThresholdBoxProps {
  specificDate: Date;
  setSpecificDate: React.Dispatch<React.SetStateAction<Date>>;
}

const SpecThresholdBox: React.FC<SpecThresholdBoxProps> = ({specificDate}) => {
  const [timeConfigData, setTimeConfigData] = useState<TimeConfig>();
  const [inputConfigData, setInputConfigData] = useState<TimeConfig>();
  const [defaultConfigData, setDefaultConfigData] = useState<TimeConfig>();
  const [useDefault, setUseDefault] = useState(true);
  const [refreshSlider, doRefreshSlider] = useState(0);

  const courseID = 1234;

  // ----------- fetch specific time Config -------------
  // fetch default and specific, if no specific, copy from default
  useEffect(() => {
    const fetchSpecTimeConfig = async (updatedDefaultConfig: TimeConfig) => {
      const [date] = specificDate.toISOString().split("T");
      try {
        const res = await axios.get("https://api.ams-lti.com/timeConfig/" + courseID + "/" + date);
        const config = res.data as TimeConfig;
        const updatedConfig = Object.fromEntries(
          Object.entries(config).map(([key, value]) => [
            key,
            typeof value === "string" ? formatTimeToISODate(value) : value,
          ])
        ) as TimeConfig;
        setTimeConfigData(updatedConfig);
        setInputConfigData(updatedConfig);
        setUseDefault(false);
      } catch (err) {
        console.error("No Specific TimeConfig:", err);
        setTimeConfigData(updatedDefaultConfig);
        setInputConfigData(updatedDefaultConfig);
        setUseDefault(true);
      }
    };

    const fetchDefaultTimeConfig = async () => {
      try {
        const res = await axios.get("https://api.ams-lti.com/timeConfig/" + courseID);
        const config = res.data as TimeConfig;
        const updatedConfig = Object.fromEntries(
          Object.entries(config).map(([key, value]) => [
            key,
            typeof value === "string" ? formatTimeToISODate(value) : value,
          ])
        ) as TimeConfig;

        setDefaultConfigData(updatedConfig);

        await fetchSpecTimeConfig(updatedConfig);
      } catch (err) {
        console.error("Error fetching timeConfigData:", err);
      }
    };

    fetchDefaultTimeConfig();
  }, [courseID, specificDate]);

  // format time string to ISO Date string
  const formatTimeToISODate = (timeVal: string | undefined): string => {
    if (!timeVal) {
      return "";
    } else {
      const [hours, minutes] = timeVal?.split(":").map(Number);

      const date = new Date();
      date.setHours(hours, minutes, 0, 0);

      return date.toISOString();
    }
  };
  // retrieve time string from ISO Date string
  const retrieveTimeFromISODate = (dateVal: string | undefined): string => {
    if (!dateVal) {
      return "";
    } else {
      const formattedDate = new Date(dateVal).toLocaleString();

      const [date, time, meridian] = formattedDate.split(" ");
      let [hours, minutes] = time.split(":").map((p) => parseInt(p));
      if (meridian === "PM" && hours !== 12) {
        hours += 12;
      } else if (meridian === "AM" && hours === 12) {
        hours = 0;
      }

      return (
        hours.toString().padStart(2, "0") +
        ":" +
        minutes.toString().padStart(2, "0") +
        ":00"
      );
    }
  };

  const handleCancel = () => {
    setInputConfigData(timeConfigData);
    doRefreshSlider(prev => prev + 1);
  };
  const handleSave = () => {
    setTimeConfigData(inputConfigData);
    const updateTimeConfig = async () => {
      const updatedConfig: TimeConfig = {
        id: inputConfigData ? inputConfigData.id : undefined,
        beginIn: inputConfigData
          ? retrieveTimeFromISODate(inputConfigData.beginIn)
          : undefined,
        endIn: inputConfigData
          ? retrieveTimeFromISODate(inputConfigData.endIn)
          : undefined,
        endLate: inputConfigData
          ? retrieveTimeFromISODate(inputConfigData.endLate)
          : undefined,
        beginOut: inputConfigData
          ? retrieveTimeFromISODate(inputConfigData.beginOut)
          : undefined,
        endOut: inputConfigData
          ? retrieveTimeFromISODate(inputConfigData.endOut)
          : undefined,
      };
      const [date] = specificDate.toISOString().split("T");

      await axios
        .put(
          "https://api.ams-lti.com/timeConfig/" + courseID + "/" + date,
          updatedConfig
        )
        .then((response) => {
          console.log("Time Config updated:", response.data);
        })
        .catch((error) => {
          console.error("Error updating time config:", error);
        });
    };
    updateTimeConfig();
  };

  const handleDefaultToggle = async () => {
    setUseDefault((prevUseDefault) => {
      if (!prevUseDefault) {
        handleRevertDefault();
      } else {
        handleSave();
      }

      return !prevUseDefault;
    });
  };

  const handleRevertDefault = async () => {
    const [date] = specificDate.toISOString().split("T");
    setTimeConfigData(defaultConfigData);
    setInputConfigData(defaultConfigData);
    await axios
      .delete("https://api.ams-lti.com/timeConfig/" + courseID + "/" + date)
      .then((response) => {
        doRefreshSlider(prev => prev + 1);
      })
      .catch((error) => {
        console.error("Error deleting time config:", error);
        doRefreshSlider(prev => prev + 1);
      });
  };

  return (
    <div>
      <DateSlider
        timeConfigData={timeConfigData}
        refresh={refreshSlider}
        disabled={useDefault}
        onChange={(value: string[]) =>
          setInputConfigData((prevData: TimeConfig | undefined) => {
            if (prevData) {
              return {...prevData, endIn: value[0], endLate: value[1], beginOut: value[2]};
            }
            return prevData;
          })
        }
      />
      <div className="spec-btn-sect">
        <div className="use-default-checkbox">
          <Checkbox
            label="Use Default Time Config"
            value={"medium"}
            checked={useDefault}
            onChange={handleDefaultToggle}
          />
        </div>
        <div className="btn-box">
          <Button
            className="sub-btn cancel-btn"
            themeOverride={{
              mediumPaddingTop: '5px',
              mediumPaddingBottom: '5px',
              transform: 'all 0.2s ease 0s',
            }}
            onClick={handleCancel}
            disabled={areTimeConfigsEqual(inputConfigData, timeConfigData)}
          >
            Cancel
          </Button>
          <Button
            className="sub-btn"
            color="primary"
            themeOverride={{
              mediumPaddingTop: '5px',
              mediumPaddingBottom: '5px',
              transform: 'all 0.2s ease 0s',
            }}
            onClick={handleSave}
            disabled={areTimeConfigsEqual(inputConfigData, timeConfigData)}
          >
            Save
          </Button>
        </div>
      </div>
    </div>
  );
};

export default SpecThresholdBox;
