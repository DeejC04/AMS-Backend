import "@testing-library/jest-dom";
import React from "react";
import { render, screen, act } from "@testing-library/react";
import SectionConfig, { CourseInfo } from "@/components/SectionConfig";
import axios from "axios";

jest.mock("axios");

describe("SectionConfig", () => {
  const mockCourseInfo: CourseInfo = {
    name: "Test Course",
    room: "Room 101",
    daysOfWeek: ["MONDAY", "WEDNESDAY"],
    startTime: "08:00:00",
    endTime: "10:00:00",
    defaultTimeConfig: {
      id: 1,
      beginIn: "08:00:00",
      endIn: "10:00:00",
      endLate: "10:15:00",
      beginOut: "12:00:00",
      endOut: "14:00:00",
    },
  };

  beforeEach(() => {
    (axios.get as jest.Mock).mockResolvedValue({ data: mockCourseInfo });
    (axios.put as jest.Mock).mockResolvedValue({ data: mockCourseInfo });
  });

  test("should render a section name", async () => {
    await act(async () => {
      render(<SectionConfig />);
      const label = await screen.findByText("Section Config");
      expect(label).toBeInTheDocument();
    });
  });
});
