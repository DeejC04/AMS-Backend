import '@testing-library/jest-dom';
import React from 'react';
import { fireEvent, render, screen } from '@testing-library/react';
import AttendanceView from '@/components/AttendanceView';

// Explanation of test suite errors:
// There are two primary errors when the test runs which seem
// unavoidable. Reasoning is explained as well
// 1. Console Log error: Happens when render is called because
// default value for data fetch is current date. Since there is
// no data, it errors
// 2. When the date is changed, an act() warning happens which is
// React's way of saying, "the value would never change manually
// like that so you should not be doing it". As of writing, this
// is hard to fix.

// Only 17th july has the required mock data but that might change
const fireDateChangeEvent = (dateValue: string) => {
  fireEvent.change(
      screen.getByLabelText('section date selector'),
      { target: { value: dateValue } }
  );
};

test('should render attendance data records', () => {
  render(<AttendanceView />);
  fireDateChangeEvent('07/17/2023');

  screen.debug()
  let attendance_table = screen.getByLabelText('attendance view table records');

  expect(attendance_table).toBeInTheDocument();
})

test('should set 17th july 2023 as date', () => {
  render(<AttendanceView />);
  fireDateChangeEvent('07/17/2023');

  let date = screen.getByLabelText('section date selector')

  expect(date.getAttribute('value')).toBe('07/17/2023')
});

test('should render AttendanceView with header "Attendance Table View"', () => {
  render(<AttendanceView />);

  const headerElement = screen.getByText('Attendance Table View');

  expect(headerElement).toBeInTheDocument();
});

test('should render filtering toggle group', () => {
  render(<AttendanceView />);

  const filteringLabel = screen.getByText('Filtering');

  expect(filteringLabel).toBeInTheDocument();
})
