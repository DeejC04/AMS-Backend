const timeConverter = (militaryTime: string): string => {
  // convert the military time to a 12 hour time
  const [hours, minutes] = militaryTime.split(":");
  const isPM = parseInt(hours) >= 12;
  const convertedHours = parseInt(hours) % 12 || 12;
  const convertedMinutes = parseInt(minutes);
  const convertedTime = `${convertedHours}:${convertedMinutes
    .toString()
    .padStart(2, "0")} ${isPM ? "PM" : "AM"}`;

  return convertedTime;
};

export default timeConverter;

// convert to UCT time
