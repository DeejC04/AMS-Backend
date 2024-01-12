import {
  AccessibleContent,
  Calendar,
  DateInput,
  IconArrowOpenEndSolid,
  IconArrowOpenStartSolid,
  IconButton,
} from "@instructure/ui";
import type { ViewProps } from '@instructure/ui-view'
import type { IconButtonProps } from '@instructure/ui-buttons'
import React, { SyntheticEvent } from "react";
import moment from "moment";

interface DateSelectProps {
  currentDate: Date;
  setCurrentDate: (date: Date) => void;
  disabled: boolean;
}

interface DateSelectState {
  value: string;
  isShowingCalendar: boolean;
  todayDate: string;
  selectedDate: string;
  renderedDate: string;
  disabledDates: string[];
  messages: any[]; // Define the appropriate type for messages
}

export class DateSelect extends React.Component<DateSelectProps, DateSelectState> {
  constructor(props:DateSelectProps) {
    super(props);
  }

  formatDate = (dateInput: string) => {
    const date = parseDate(dateInput);
    return `${date.format("MM/DD/YYYY")}`;
  };

  state = {
    value: this.formatDate(this.props.currentDate.toISOString()),
    isShowingCalendar: false,
    todayDate: this.props.currentDate.toISOString(),
    selectedDate: this.props.currentDate.toISOString(),
    renderedDate: this.props.currentDate.toISOString(), //parseDate("2019-08-01").toISOString(),
    disabledDates: [],
    messages: [],
  };

  generateMonth = (renderedDate = this.state.renderedDate) => {
    const date = parseDate(renderedDate).startOf("month").startOf("week");

    return Array.apply(null, Array(Calendar.DAY_COUNT)).map(() => {
      const currentDate = date.clone();
      date.add(1, "days");
      return currentDate;
    });
  };

  handleChange = (event: React.SyntheticEvent<Element, Event>, { value }: { value: string }) => {
    const newDateStr = parseDate(value).toISOString();

    this.props.setCurrentDate(new Date(newDateStr));

    this.setState(({ renderedDate }) => ({
      value,
      selectedDate: newDateStr,
      renderedDate: newDateStr || renderedDate,
      messages: [],
    }));
  };

  handleShowCalendar = (event: React.SyntheticEvent<Element, Event>) => {
    this.setState({ isShowingCalendar: true });
  };

  handleHideCalendar = (event: React.SyntheticEvent<Element, Event>) => {
    this.setState(({ selectedDate, disabledDates, value }) => ({
      isShowingCalendar: false,
      value: selectedDate ? this.formatDate(selectedDate) : value,
    }));
  };

  handleValidateDate = (event: React.SyntheticEvent<Element, Event>) => {
    this.setState((prevState) => {
      const { selectedDate, value } = prevState;
      let messages: any = [];
  
      if (!selectedDate && value) {
        messages = [{ type: "error", text: "This date is invalid" }];
      }
      if (selectedDate && this.isDisabledDate(parseDate(selectedDate))) {
        messages = [{ type: "error", text: "This date is disabled" }];
      }
  
      // Return a new state object with updated messages
      return {
        ...prevState, // Spread the previous state to retain its other properties
        messages,
      };
    });
  };

  handleDayClick = (event: React.MouseEvent<ViewProps, MouseEvent>, { date }:{date: string}) => {
    this.props.setCurrentDate(new Date(date));
    this.setState({
      selectedDate: date,
      renderedDate: date,
      messages: [],
    });
  };

  handleSelectNextDay = (event: React.SyntheticEvent<Element, Event> | React.KeyboardEvent<ViewProps> | React.MouseEvent<ViewProps>) => {
    this.modifySelectedDate("day", 1);
  };

  handleSelectPrevDay = (event: React.KeyboardEvent<ViewProps> | React.MouseEvent<ViewProps> | SyntheticEvent) => {
    this.modifySelectedDate("day", -1);
  };

  handleRenderNextMonth = (event: React.SyntheticEvent<Element, Event>) => {
    this.modifyRenderedDate("month", 1);
  };

  handleRenderPrevMonth = (event: React.SyntheticEvent<Element, Event>) => {
    this.modifyRenderedDate("month", -1);
  };

  modifyRenderedDate = (type: moment.unitOfTime.DurationConstructor, step: number) => {
    this.setState(({ renderedDate }) => {
      return { renderedDate: this.modifyDate(renderedDate, type, step) };
    });
  };

  modifySelectedDate = (type: moment.unitOfTime.DurationConstructor, step: number) => {
    this.setState(({ selectedDate, renderedDate }) => {
      // We are either going to increase or decrease our selectedDate by 1 day.
      // If we do not have a selectedDate yet, we'll just select the first day of
      // the currently rendered month instead.
      const newDate = selectedDate
        ? this.modifyDate(selectedDate, type, step)
        : parseDate(renderedDate).startOf("month").toISOString();

      this.props.setCurrentDate(new Date(newDate));

      return {
        selectedDate: newDate,
        renderedDate: newDate,
        value: this.formatDate(newDate),
        messages: [],
      };
    });
  };

  modifyDate = (dateStr: string, type: moment.unitOfTime.DurationConstructor, step: number) => {
    const date: moment.Moment = parseDate(dateStr);
    date.add(step, type);
    return date.toISOString();
  };

  isDisabledDate = (date: moment.Moment, disabledDates = this.state.disabledDates) => {
    return disabledDates.reduce((result, disabledDate) => {
      return result || date.isSame(disabledDate, "day");
    }, false);
  };

  renderWeekdayLabels = () => {
    const date = parseDate(this.state.renderedDate).startOf("week");

    return Array.apply(null, Array(7)).map(() => {
      const currentDate = date.clone();
      date.add(1, "day");

      return (
        <AccessibleContent alt={currentDate.format("dddd")} key={currentDate.format("dddd")}>
          {currentDate.format("dd")}
        </AccessibleContent>
      );
    });
  };

  renderDays() {
    const { renderedDate, selectedDate, todayDate } = this.state;

    return this.generateMonth().map((date) => {
      const dateStr = date.toISOString();

      return (
        <DateInput.Day
          key={dateStr}
          date={dateStr}
          interaction={this.isDisabledDate(date) ? "disabled" : "enabled"}
          isSelected={date.isSame(selectedDate, "day")}
          isToday={date.isSame(todayDate, "day")}
          isOutsideMonth={!date.isSame(renderedDate, "month")}
          label={`${date.format("D")} ${date.format("MMMM")} ${date.format(
            "YYYY"
          )}`}
          onClick={(event) => this.handleDayClick(event, { date: dateStr })}
        >
          {date.format("D")}
        </DateInput.Day>
      );
    });
  }

  render() {
    const { value, isShowingCalendar, renderedDate, messages } = this.state;

    const date = parseDate(this.state.renderedDate);

    const buttonProps = (type = "prev"): IconButtonProps => ({
      size: "small",
      withBackground: false,
      withBorder: false,
      renderIcon:
        type === "prev" ? (
          <IconArrowOpenStartSolid color="primary" />
        ) : (
          <IconArrowOpenEndSolid color="primary" />
        ),
      screenReaderLabel: type === "prev" ? "Previous month" : "Next month",
    });

    return (
      <div className="date-input-box">
      <IconButton
        className="triangle-button"
        {...buttonProps("prev")}
        size="medium"
        onClick={this.handleSelectPrevDay}
      />
        <DateInput
          renderLabel=""
          aria-label="section date selector"
          assistiveText="Type a date or use arrow keys to navigate date picker."
          value={value}
          onChange={this.handleChange}
          width="10rem"
          isInline
          messages={messages}
          isShowingCalendar={isShowingCalendar}
          onRequestValidateDate={this.handleValidateDate}
          onRequestShowCalendar={this.handleShowCalendar}
          onRequestHideCalendar={this.handleHideCalendar}
          onRequestSelectNextDay={this.handleSelectNextDay}
          onRequestSelectPrevDay={this.handleSelectPrevDay}
          onRequestRenderNextMonth={this.handleRenderNextMonth}
          onRequestRenderPrevMonth={this.handleRenderPrevMonth}
          renderNavigationLabel={
            <span>
              <div>{date.format("MMMM")}</div>
              <div>{date.format("YYYY")}</div>
            </span>
          }
          renderPrevMonthButton={<IconButton {...buttonProps("prev")} />}
          renderNextMonthButton={<IconButton {...buttonProps("next")} />}
          renderWeekdayLabels={this.renderWeekdayLabels()}
          interaction={this.props.disabled? "disabled": "enabled"}
        >
          {this.renderDays()}
        </DateInput>
        <IconButton
            className="triangle-button"
          {...buttonProps("next")}
          size="medium"
          onClick={this.handleSelectNextDay}
        />
      </div>
    );
  }
}

const locale = "en-us";
const timezone = "America/Denver";

const parseDate = (dateStr: string) => {
  return moment.tz(
    dateStr,
    [moment.ISO_8601, "llll", "LLLL", "lll", "LLL", "ll", "LL", "l", "L"],
    locale,
    timezone
  );
};
