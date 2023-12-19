import '@testing-library/jest-dom';
import React from 'react';
import { render, screen } from '@testing-library/react';
import AttendanceView from '@/components/AttendanceView';

test('renders AttendanceView with header "Attendance Table View"', () => {
  render(<AttendanceView />);

  const headerElement = screen.getByText('Attendance Table View');

  expect(headerElement).toBeInTheDocument();
});
