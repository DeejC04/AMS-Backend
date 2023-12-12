import React, { createContext, useState } from "react";

export type DateContextType = {
  currentDate: Date;
  setCurrentDate: (date: Date) => void;
};

export const DateContext = createContext<DateContextType>({
  // get current date in month/day/year format
  currentDate: new Date(),
  setCurrentDate: () => {},
});

type DateProviderProps = {
  children: React.ReactNode;
};

const DateProvider = ({ children }: DateProviderProps) => {
  const [currentDate, setCurrentDate] = useState<Date>(new Date());

  const value: DateContextType = {
    currentDate,
    setCurrentDate,
  };

  return <DateContext.Provider value={value}>{children}</DateContext.Provider>;
};

export default DateProvider;
