import React, {useContext, useState} from "react";
import {RiArrowUpSLine, RiArrowDownSLine} from "react-icons/ri";
import ThresholdBox from "./ThresholdBox";
import {VscTriangleLeft, VscTriangleRight} from "react-icons/vsc";
import type {DateContextType} from "@/contexts/DateContext";
import {DateContext} from "@/contexts/DateContext";
import SpecThresholdBox from "./SpecThresholdBox";
import {DateSelect} from "./DateSelect";

const SpecConfigBox = () => {
  const {currentDate} = useContext<DateContextType>(DateContext);

  const [specificDate, setSpecificDate] = useState<Date>(currentDate);

  return (
    <div className="specific-config-box">
      <div
        className="date-input-box"
      >
        <DateSelect
          currentDate={specificDate}
          setCurrentDate={setSpecificDate}
          disabled={false}
        ></DateSelect>
      </div>

      <SpecThresholdBox
        specificDate={specificDate}
        setSpecificDate={setSpecificDate}
      />
    </div>
  );
};

export default SpecConfigBox;
