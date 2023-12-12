import React, {useEffect, useState,} from "react";
import ReactSlider from "react-slider";
import {TimeConfig} from "@/components/ThresholdBox";
import {is} from "immutable";

function minutesToTime(minutes: number) {
  const hours = Math.floor(minutes / 60);
  const mins = minutes % 60;

  const ampm = hours >= 12 ? "PM" : "AM";
  const formattedHours = hours % 12 === 0 ? 12 : hours % 12;

  return `${formattedHours}:${mins < 10 ? "0" : ""}${mins} ${ampm}`;
}

interface IntervalSliderProps {
  timeConfigData: TimeConfig | undefined;
  refresh: number;
  disabled: boolean;
  onChange: (value: string[]) => void;
}

const DateSlider: React.FC<IntervalSliderProps> = (props) => {
  const [day, setDay] = useState<Date>(new Date(0));
  const [value, setValue] = useState([0, 30, 60])
  const [beginIn, setBeginIn] = useState(0);
  const [endOut, setEndOut] = useState(60);
  const [hoveredTrack, setHoveredTrack] = useState<number | null>(null);
  const [activeThumb, setActiveThumb] = useState<number | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const getTimeStamp = (date: Date) => {
      return (60 * date.getHours()) + date.getMinutes();
    }
    if (props.timeConfigData &&
      props.timeConfigData.beginIn &&
      props.timeConfigData.endIn &&
      props.timeConfigData.endLate &&
      props.timeConfigData.beginOut &&
      props.timeConfigData.endOut) {
      setLoading(true);

      const beginInDate = new Date(props.timeConfigData.beginIn);
      const endInDate = new Date(props.timeConfigData.endIn);
      const endLateDate = new Date(props.timeConfigData.endLate);
      const beginOutDate = new Date(props.timeConfigData.beginOut);
      const endOutDate = new Date(props.timeConfigData.endOut);

      setDay(new Date(beginInDate.toDateString()));
      const beginInTimestamp = getTimeStamp(beginInDate);
      const endOutTimestamp = getTimeStamp(endOutDate);
      const valueTimestamps = [getTimeStamp(endInDate), getTimeStamp(endLateDate), getTimeStamp(beginOutDate)]

      setBeginIn(beginInTimestamp);
      setEndOut(endOutTimestamp);
      setLoading(false)
      onSliderChange(valueTimestamps);
    }
  }, [props.timeConfigData, props.refresh, loading]);

  const onSliderChange = (valueTimeStamps: number[]) => {
    if (loading) {
      return;
    }
    setValue(valueTimeStamps);

    let dateValue = [];

    for (let i = 0; i < valueTimeStamps.length; i++) {
      dateValue.push((new Date(day.getTime() + valueTimeStamps[i] * 60000)).toISOString());
    }

    props.onChange(dateValue);
  }

  const Thumb: React.FC<any> = (props, state) => {
    return (
      <div
        {...props}
        onMouseDown={() => setActiveThumb(state.index)}
        onMouseOver={() => setActiveThumb(state.index)}
        onMouseLeave={() => setActiveThumb(null)}
      >
        <div className={`thumb-hover-area thumb-hover-area-${state.index}`}/>
        <div className="time-tooltip"><p>{minutesToTime(state.valueNow)}</p></div>
      </div>
    );
  }

  const Track: React.FC<any> = (props, state) => {
    const isHovered = (activeThumb === null && hoveredTrack === state.index) ||
      (activeThumb !== null && (activeThumb === state.index || activeThumb + 1 === state.index));
    return (
      <div
        {...props}
        onMouseEnter={() => setHoveredTrack(state.index)}
        onMouseLeave={() => setHoveredTrack(null)}
      >
        <div className={`track-hover-area track-hover-area-${state.index}`}/>
        <div className={`track-tooltip-container track-tooltip-container-${state.index} ${isHovered ? 'hovered' : ''}`}>
          <div className={"track-tooltip" + " track-tooltip-" + state.index}>
            <p>
              {state.index === 0 && "Arrived On Time"}
              {state.index === 1 && "Arrived Late"}
              {state.index === 2 && "Left Early"}
              {state.index === 3 && "Left On Time"}
            </p>
          </div>
        </div>
      </div>
    );
  }
  return (
    <div className={`slider-container ${props.disabled || day.getTime() === 0 ? "disabled" : ""}`}>
      <ReactSlider
        className="horizontal-slider"
        thumbClassName="thumb"
        trackClassName="track"
        value={value}
        renderTrack={Track}
        renderThumb={Thumb}
        onChange={onSliderChange}
        minDistance={5}
        step={5}
        min={beginIn}
        max={endOut}
      />
    </div>
  );
}

export default DateSlider;