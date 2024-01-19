import React, { useState } from "react";
import type { NextPage } from "next";

// components
import AttendanceView from "@/components/AttendanceView";
import { ConfigTray } from "@/components/ConfigTray";
import { PairTray } from "@/components/PairTray";

type CourseInfo = {
  // add more attributes moving forward
  startTime: string | undefined;
  endTime: string | undefined;
};

const Home: NextPage = () => {
  const [dateValue, setDateValue] = useState<Date | Date[]>(new Date());
  // const [courseData, setCourseData] = useState<CourseInfo | undefined>();

  return (
    <div className="mt-20">
      <AttendanceView/>
      <ConfigTray/>
      <PairTray/>
    </div>
  );
};

export default Home;
