import React, {
  useEffect,
  useState,
} from "react";

// styles
import axios from "axios";
import {RiArrowDownSLine, RiArrowUpSLine} from "react-icons/ri";
import DateSlider from "@/components/DateSlider";
import {Button} from "@instructure/ui";
import styles from "@/styles/TimeView.module.scss";
import {TimeSelect} from "@instructure/ui-time-select";
import {Alert, CloseButton} from "@instructure/ui";

export type TimeConfig = {
  // add more attributes moving forward
  id: number | undefined;
  beginIn: string | undefined;
  endIn: string | undefined;
  endLate: string | undefined;
  beginOut: string | undefined;
  endOut: string | undefined;
};
export const areTimeConfigsEqual = (config1: TimeConfig | undefined, config2: TimeConfig | undefined): boolean => {
  return (
    (config1 === undefined && config2 === undefined) ||
    config1 !== undefined && config2 !== undefined &&
    config1.beginIn === config2.beginIn &&
    config1.endIn === config2.endIn &&
    config1.endLate === config2.endLate &&
    config1.beginOut === config2.beginOut &&
    config1.endOut === config2.endOut
  );
};

export type BoundError = {
  id: string | undefined;
  isError: boolean | undefined;
  msg: string | undefined;
};

const ThresholdBox = () => {
  const [timeConfigData, setTimeConfigData] = useState<TimeConfig>();
  const [inputConfigData, setInputConfigData] = useState<TimeConfig>();
  const [error, setError] = useState<BoundError>();
  const [refreshSlider, doRefreshSlider] = useState(0);

  const courseID = 1234;

  // ----------- fetch time Config -------------
  useEffect(() => {
    const fetchTimeConfig = async () => {
      await axios
        .get("https://api.ams-lti.com/timeConfig/" + courseID)
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
          console.error("Error fetching timeConfigData:", err);
        });
    };
    fetchTimeConfig();
  }, [courseID]);

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

  const handleBeginInChange = (
    e: React.SyntheticEvent<Element, Event>,
    {value}: { value: string | undefined }
  ) => {
    // Check if the selected start time is within range
    console.log(inputConfigData, error)
    const endIn = inputConfigData?.endIn;
    if (endIn && value && value > endIn) {
      setError({id: "beginIn", isError: true, msg: "Begin In cannot be later than end In"});
      return;
    }
    // the type errors are because of mock data, once pulled from api - will add the constraints
    setInputConfigData((prevData: TimeConfig | undefined) => {
      if (prevData) {
        return {...prevData, beginIn: value};
      }
      return prevData;
    });
    setError({id: "", isError: false, msg: ""});
  };

  const handleEndInChange = (
    e: React.SyntheticEvent<Element, Event>,
    {value}: { value: string | undefined }
  ) => {
    // Check if the selected End time is within range
    const beginIn = inputConfigData?.beginIn;
    const endLate = inputConfigData?.endLate;
    if (beginIn && value && endLate && (value < beginIn || value > endLate)) {
      setError({id: "endIn", isError: true, msg: "End In cannot be earlier than Begin In or later than End Late"});
      return;
    }
    // the type errors are because of mock data, once pulled from api - will add the constraints
    setInputConfigData((prevData: TimeConfig | undefined) => {
      if (prevData) {
        return {...prevData, endIn: value};
      }
      return prevData;
    });
    setError({id: "", isError: false, msg: ""});
  };

  const handleEndLateChange = (
    e: React.SyntheticEvent<Element, Event>,
    {value}: { value: string | undefined }
  ) => {
    // Check if the selected End time is within range
    const endIn = inputConfigData?.endIn;
    const beginOut = inputConfigData?.beginOut;
    if (endIn && value && beginOut && (value < endIn || value > beginOut)) {
      setError({id: "endLate", isError: true, msg: "End Late cannot be earlier than End In or later than Begin Out"});
      return;
    }
    // the type errors are because of mock data, once pulled from api - will add the constraints
    setInputConfigData((prevData: TimeConfig | undefined) => {
      if (prevData) {
        return {...prevData, endLate: value};
      }
      return prevData;
    });
    setError({id: "", isError: false, msg: ""});
  };

  const handleBeginOutChange = (
    e: React.SyntheticEvent<Element, Event>,
    {value}: { value: string | undefined }
  ) => {
    // Check if the selected End time is within range
    const endLate = inputConfigData?.endLate;
    const endOut = inputConfigData?.endOut;
    if (endLate && value && endOut && (value < endLate || value > endOut)) {
      setError({id: "beginOut", isError: true, msg: "Begin Out cannot be earlier than End Late or later than End Out"});
      return;
    }
    // the type errors are because of mock data, once pulled from api - will add the constraints
    setInputConfigData((prevData: TimeConfig | undefined) => {
      if (prevData) {
        return {...prevData, beginOut: value};
      }
      return prevData;
    });
    setError({id: "", isError: false, msg: ""});
  };

  const handleEndOutChange = (
    e: React.SyntheticEvent<Element, Event>,
    {value}: { value: string | undefined }
  ) => {
    // Check if the selected End time is within range
    const beginOut = inputConfigData?.beginOut;
    if (beginOut && value && value < beginOut) {
      setError({id: "endOut", isError: true, msg: "End Out cannot be earlier than Begin Out"});
      return;
    }
    // the type errors are because of mock data, once pulled from api - will add the constraints
    setInputConfigData((prevData: TimeConfig | undefined) => {
      if (prevData) {
        return {...prevData, endOut: value};
      }
      return prevData;
    });
    setError({id: "", isError: false, msg: ""});
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
      // const updatedConfig: TimeConfig = {
      //   id: undefined,
      //   beginIn: retrieveTimeFromISODate("2023-11-22T17:20:00.000Z"),
      //   endIn: retrieveTimeFromISODate("2023-11-22T18:20:00.000Z"),
      //   endLate: retrieveTimeFromISODate("2023-11-22T19:25:00.000Z"),
      //   beginOut: retrieveTimeFromISODate("2023-11-22T20:30:00.000Z"),
      //   endOut: retrieveTimeFromISODate("2023-11-22T21:15:00.000Z"),
      // };

      await axios
        .put("https://api.ams-lti.com/timeConfig/" + courseID, updatedConfig)
        .then((response) => {
          console.log("Time Config updated:", response.data);
        })
        .catch((error) => {
          console.error("Error updating time config:", error);
        });
    };
    updateTimeConfig();
  };
  return (
    <div>
      <div className="expand-label">
        Default Time Config
      </div>
      <div
        className="specific-config-box"
      >
        <DateSlider
          timeConfigData={timeConfigData}
          refresh={refreshSlider}
          disabled={false}
          onChange={(value: string[]) =>
            setInputConfigData((prevData: TimeConfig | undefined) => {
              if (prevData) {
                return {...prevData, endIn: value[0], endLate: value[1], beginOut: value[2]};
              }
              return prevData;
            })
          }
        />
        {/*<table className={styles.table}>*/}
        {/*  <tbody className={styles.grid}>*/}
        {/*  <tr>*/}
        {/*    <td>*/}
        {/*      <TimeSelect*/}
        {/*        renderLabel="Begin In"*/}
        {/*        id="beginIn"*/}
        {/*        step={5}*/}
        {/*        onChange={(e, {value}) =>*/}
        {/*          handleBeginInChange(e, {value})*/}
        {/*        }*/}
        {/*        onInputChange={(e, value, isoValue) =>*/}
        {/*          setInputConfigData((prevData: TimeConfig | undefined) => {*/}
        {/*            if (prevData) {*/}
        {/*              return {...prevData, beginIn: isoValue};*/}
        {/*            }*/}
        {/*            return prevData;*/}
        {/*          })*/}
        {/*        }*/}
        {/*        value={inputConfigData?.beginIn}*/}
        {/*        allowNonStepInput*/}
        {/*      />*/}
        {/*    </td>*/}
        {/*    <td>*/}
        {/*      <TimeSelect*/}
        {/*        renderLabel="End In"*/}
        {/*        id="endIn"*/}
        {/*        step={5}*/}
        {/*        onChange={(e, {value}) => handleEndInChange(e, {value})}*/}
        {/*        onInputChange={(e, value, isoValue) =>*/}
        {/*          setInputConfigData((prevData: TimeConfig | undefined) => {*/}
        {/*            if (prevData) {*/}
        {/*              return {...prevData, endIn: isoValue};*/}
        {/*            }*/}
        {/*            return prevData;*/}
        {/*          })*/}
        {/*        }*/}
        {/*        value={inputConfigData?.endIn}*/}
        {/*        allowNonStepInput*/}
        {/*      />*/}
        {/*    </td>*/}
        {/*    <td>*/}
        {/*      <TimeSelect*/}
        {/*        renderLabel="End Late"*/}
        {/*        id="endLate"*/}
        {/*        step={5}*/}
        {/*        onChange={(e, {value}) =>*/}
        {/*          handleEndLateChange(e, {value})*/}
        {/*        }*/}
        {/*        onInputChange={(e, value, isoValue) =>*/}
        {/*          setInputConfigData((prevData: TimeConfig | undefined) => {*/}
        {/*            if (prevData) {*/}
        {/*              return {...prevData, endLate: isoValue};*/}
        {/*            }*/}
        {/*            return prevData;*/}
        {/*          })*/}
        {/*        }*/}
        {/*        value={inputConfigData?.endLate}*/}
        {/*        allowNonStepInput*/}
        {/*      />*/}
        {/*    </td>*/}
        {/*  </tr>*/}
        {/*  <tr>*/}
        {/*    <td>*/}
        {/*      <TimeSelect*/}
        {/*        renderLabel="Begin Out"*/}
        {/*        id="beginOut"*/}
        {/*        step={5}*/}
        {/*        onChange={(e, {value}) =>*/}
        {/*          handleBeginOutChange(e, {value})*/}
        {/*        }*/}
        {/*        onInputChange={(e, value, isoValue) =>*/}
        {/*          setInputConfigData((prevData: TimeConfig | undefined) => {*/}
        {/*            if (prevData) {*/}
        {/*              return {...prevData, beginOut: isoValue};*/}
        {/*            }*/}
        {/*            return prevData;*/}
        {/*          })*/}
        {/*        }*/}
        {/*        value={inputConfigData?.beginOut}*/}
        {/*        allowNonStepInput*/}
        {/*      />*/}
        {/*    </td>*/}
        {/*    <td>*/}
        {/*      <TimeSelect*/}
        {/*        renderLabel="End Out"*/}
        {/*        id="endOut"*/}
        {/*        step={5}*/}
        {/*        onChange={(e, {value}) =>*/}
        {/*          handleEndOutChange(e, {value})*/}
        {/*        }*/}
        {/*        onInputChange={(e, value, isoValue) =>*/}
        {/*          setInputConfigData((prevData: TimeConfig | undefined) => {*/}
        {/*            if (prevData) {*/}
        {/*              return {...prevData, endOut: isoValue};*/}
        {/*            }*/}
        {/*            return prevData;*/}
        {/*          })*/}
        {/*        }*/}
        {/*        value={inputConfigData?.endOut}*/}
        {/*        allowNonStepInput*/}
        {/*      />*/}
        {/*    </td>*/}
        {/*  </tr>*/}
        {/*  </tbody>*/}
        {/*</table>*/}
        {/*{error && error.isError && (*/}
        {/*  <Alert*/}
        {/*    variant="error"*/}
        {/*    margin="small"*/}
        {/*    timeout={5000}*/}
        {/*    renderCloseButtonLabel="Close"*/}
        {/*    onDismiss={() => setError({id: "", isError: false, msg: ""})}*/}
        {/*  >*/}
        {/*    {error.msg}*/}
        {/*  </Alert>*/}
        {/*)}*/}
        <div className="btn-box">
          <Button
            className="sub-btn cancel-btn"
            themeOverride={{
              mediumPaddingTop: '5px',
              mediumPaddingBottom: '5px',
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
              mediumPaddingBottom: '5px'
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

export default ThresholdBox;
