import React, {
  useMemo,
  useEffect,
  useState,
  useCallback,
  ChangeEvent,
  useContext,
} from "react";
import { TimeSelect } from "@instructure/ui-time-select";
import type { DateContextType } from "@/contexts/DateContext";
import { DateContext } from "@/contexts/DateContext";
import { TimeConfig, BoundError } from "./ThresholdBox";

// styles
import styles from "@/styles/TimeView.module.scss";
import axios from "axios";
import { Alert } from "@instructure/ui";

interface SpecThresholdBoxProps {
  specificDate: Date;
  setSpecificDate: React.Dispatch<React.SetStateAction<Date>>;
}

const SpecThresholdBox: React.FC<SpecThresholdBoxProps> = ({
  specificDate,
  setSpecificDate,
}) => {
  const [timeConfigData, setTimeConfigData] = useState<TimeConfig>();
  const [inputConfigData, setInputConfigData] = useState<TimeConfig>();
  const [defaultConfigData, setDefaultConfigData] = useState<TimeConfig>();
  const [error, setError] = useState<BoundError>();

  const courseID = 1234;

  // ----------- fetch specific time Config -------------
  // fetch default and specific, if no specific, copy from default
  useEffect(() => {
    const fetchSpecTimeConfig = async () => {
      const [date] = specificDate.toISOString().split("T");
      await axios
        .get("https://api.ams-lti.com/timeConfig/" + courseID + "/" + date)
        .then((res) => {
          const config = res.data as TimeConfig;
          const updatedConfig = Object.fromEntries(
            Object.entries(config).map(([key, value]) => [
              key,
              typeof value === "string" ? formatTimeToISODate(value) : value,
            ])
          ) as TimeConfig;

          setTimeConfigData(updatedConfig);
          setInputConfigData(updatedConfig);
        })
        .catch((err) => {
          console.error("No Specific TimeConfig:", err);
        });
    };
    const fetchDefaultTimeConfig = async () => {
      await axios
        .get("https://api.ams-lti.com/timeConfig/" + courseID)
        .then((res) => {
          const config = res.data as TimeConfig;
          const updatedConfig = Object.fromEntries(
            Object.entries(config).map(([key, value]) => [
              key,
              typeof value === "string"
                ? formatTimeToISODate(value)
                : value,
            ])
          ) as TimeConfig;

          setTimeConfigData(updatedConfig);
          setInputConfigData(updatedConfig);
          setDefaultConfigData(updatedConfig);

          fetchSpecTimeConfig();
        })
        .catch((err) => {
          console.error("Error fetching timeConfigData:", err);
          fetchSpecTimeConfig();
        });
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

      // console.log(date.toISOString());

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

  const handleBeginInChange = (
    e: React.SyntheticEvent<Element, Event>,
    { value }: { value: string | undefined }
  ) => {
    // Check if the selected start time is within range
    // console.log(timeConfigData)
    const endIn = inputConfigData?.endIn;
    if (endIn && value && value > endIn) {
      setError({ id: "beginIn", isError: true, msg: "Begin In cannot be later than end In" });
      return;
    }
    // the type errors are because of mock data, once pulled from api - will add the constraints
    setInputConfigData((prevData: TimeConfig | undefined) => {
      if (prevData) {
        return { ...prevData, beginIn: value };
      }
      return prevData;
    });
    setError({id: "", isError: false, msg: ""});
  };

  const handleEndInChange = (
    e: React.SyntheticEvent<Element, Event>,
    { value }: { value: string | undefined }
  ) => {
    // Check if the selected End time is within range
    const beginIn = inputConfigData?.beginIn;
    const endLate = inputConfigData?.endLate;
    if (beginIn && value && endLate && (value < beginIn || value > endLate)) {
      setError({ id: "endIn", isError: true, msg: "End In cannot be earlier than Begin In or later than End Late" });
      return;
    }
    // the type errors are because of mock data, once pulled from api - will add the constraints
    setInputConfigData((prevData: TimeConfig | undefined) => {
      if (prevData) {
        return { ...prevData, endIn: value };
      }
      return prevData;
    });
    setError({id: "", isError: false, msg: ""});
  };

  const handleEndLateChange = (
    e: React.SyntheticEvent<Element, Event>,
    { value }: { value: string | undefined }
  ) => {
    // Check if the selected End time is within range
    const endIn = inputConfigData?.endIn;
    const beginOut = inputConfigData?.beginOut;
    if (endIn && value && beginOut && (value < endIn || value > beginOut)) {
      setError({ id: "endLate", isError: true, msg: "End Late cannot be earlier than End In or later than Begin Out" });
      return;
    }
    // the type errors are because of mock data, once pulled from api - will add the constraints
    setInputConfigData((prevData: TimeConfig | undefined) => {
      if (prevData) {
        return { ...prevData, endLate: value };
      }
      return prevData;
    });
    setError({id: "", isError: false, msg: ""});
  };

  const handleBeginOutChange = (
    e: React.SyntheticEvent<Element, Event>,
    { value }: { value: string | undefined }
  ) => {
    // Check if the selected End time is within range
    const endLate = inputConfigData?.endLate;
    const endOut = inputConfigData?.endOut;
    if (endLate && value && endOut && (value < endLate || value > endOut)) {
      setError({ id: "beginOut", isError: true, msg: "Begin Out cannot be earlier than End Late or later than End Out" });
      return;
    }
    // the type errors are because of mock data, once pulled from api - will add the constraints
    setInputConfigData((prevData: TimeConfig | undefined) => {
      if (prevData) {
        return { ...prevData, beginOut: value };
      }
      return prevData;
    });
    setError({id: "", isError: false, msg: ""});
  };

  const handleEndOutChange = (
    e: React.SyntheticEvent<Element, Event>,
    { value }: { value: string | undefined }
  ) => {
    // Check if the selected End time is within range
    const beginOut = inputConfigData?.beginOut;
    if (beginOut && value && value < beginOut) {
      setError({ id: "endOut", isError: true, msg: "End Out cannot be earlier than Begin Out" });
      return;
    }
    // the type errors are because of mock data, once pulled from api - will add the constraints
    setInputConfigData((prevData: TimeConfig | undefined) => {
      if (prevData) {
        return { ...prevData, endOut: value };
      }
      return prevData;
    });
    setError({id: "", isError: false, msg: ""});
  };

  const handleCancel = () => {
    setInputConfigData(timeConfigData);
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

  const handleRevertDefault = async () => {
    const [date] = specificDate.toISOString().split("T");
    await axios
      .delete("https://api.ams-lti.com/timeConfig/" + courseID + "/" + date)
      .then((response) => {
        console.log("Specific Time Config deleted:", response.data);
        
        setTimeConfigData(defaultConfigData)
        setInputConfigData(defaultConfigData)
      })
      .catch((error) => {
        console.error("Error deleting time config:", error);
      });
  };

  return (
    <div>
      <table className={styles.table}>
        <tbody className={styles.grid}>
          <tr>
            <td className={defaultConfigData?.beginIn !== inputConfigData?.beginIn? "time-select": ""}>
              <TimeSelect
                renderLabel="Begin In"
                id="beginIn"
                step={5}
                onChange={(e, { value }) => handleBeginInChange(e, { value })}
                onInputChange={(e, value, isoValue) =>
                  setInputConfigData((prevData: TimeConfig | undefined) => {
                    if (prevData) {
                      return { ...prevData, beginIn: isoValue };
                    }
                    return prevData;
                  })
                }
                value={inputConfigData?.beginIn}
                allowNonStepInput
              />
            </td>
            <td className={defaultConfigData?.endIn !== inputConfigData?.endIn? "time-select": ""}>
              <TimeSelect
                renderLabel="End In"
                id="endIn"
                step={5}
                onChange={(e, { value }) => handleEndInChange(e, { value })}
                onInputChange={(e, value, isoValue) =>
                  setInputConfigData((prevData: TimeConfig | undefined) => {
                    if (prevData) {
                      return { ...prevData, endIn: isoValue };
                    }
                    return prevData;
                  })
                }
                value={inputConfigData?.endIn}
                allowNonStepInput
              />
            </td>
            <td className={defaultConfigData?.endLate !== inputConfigData?.endLate? "time-select": ""}>
              <TimeSelect
                renderLabel="End Late"
                id="endLate"
                step={5}
                onChange={(e, { value }) => handleEndLateChange(e, { value })}
                onInputChange={(e, value, isoValue) =>
                  setInputConfigData((prevData: TimeConfig | undefined) => {
                    if (prevData) {
                      return { ...prevData, endLate: isoValue };
                    }
                    return prevData;
                  })
                }
                value={inputConfigData?.endLate}
                allowNonStepInput
              />
            </td>
          </tr>
          <tr>
            <td className={defaultConfigData?.beginOut !== inputConfigData?.beginOut? "time-select": ""}>
              <TimeSelect
                renderLabel="Begin Out"
                id="beginOut"
                step={5}
                onChange={(e, { value }) => handleBeginOutChange(e, { value })}
                onInputChange={(e, value, isoValue) =>
                  setInputConfigData((prevData: TimeConfig | undefined) => {
                    if (prevData) {
                      return { ...prevData, beginOut: isoValue };
                    }
                    return prevData;
                  })
                }
                value={inputConfigData?.beginOut}
                allowNonStepInput
              />
            </td>
            <td className={defaultConfigData?.endOut !== inputConfigData?.endOut? "time-select": ""}>
              <TimeSelect
                renderLabel="End Out"
                id="endOut"
                step={5}
                onChange={(e, { value }) => handleEndOutChange(e, { value })}
                onInputChange={(e, value, isoValue) =>
                  setInputConfigData((prevData: TimeConfig | undefined) => {
                    if (prevData) {
                      return { ...prevData, endOut: isoValue };
                    }
                    return prevData;
                  })
                }
                value={inputConfigData?.endOut}
                allowNonStepInput
              />
            </td>
          </tr>
        </tbody>
      </table>
      {error && error.isError && (
            <Alert
              variant="error"
              margin="small"
              timeout={5000}
              renderCloseButtonLabel="Close"
              onDismiss={() => setError({id: "", isError: false, msg: ""})}
            >
              {error.msg}
            </Alert>
          )}
      <div className="spec-btn-sect">
        <button className="sub-btn default-btn" onClick={handleRevertDefault}>
          Back to Default
        </button>
        <div className="btn-box">
          <button
            className="sub-btn cancel-btn"
            onClick={handleCancel}
            disabled={inputConfigData === timeConfigData}
          >
            Cancel
          </button>
          <button
            className="sub-btn"
            onClick={handleSave}
            disabled={inputConfigData === timeConfigData}
          >
            Save
          </button>
        </div>
      </div>
    </div>
  );
};

export default SpecThresholdBox;
