import React from "react";
import Select, { components, GroupHeadingProps } from "react-select";

// Define the SectionOption type
type SectionOption = { label: string; value: string };

// Define the GroupedOption type
type GroupedOption = {
  label: string;
  options: SectionOption[];
};

interface SectionSelectProps {
  groupedOptions: GroupedOption[];
}

const groupStyles = {
  border: `5px solid #42404e`,
  color: "white",
  //   background: "#38316f",
  background: "#42404e",
  padding: "5px 0px",
  display: "flex",
};

const GroupHeading = (props: GroupHeadingProps<SectionOption>) => (
  <div style={groupStyles}>
    <components.GroupHeading {...props} />
  </div>
);

const SectionSelect: React.FC<SectionSelectProps> = ({ groupedOptions }) => {
  const defaultOption: SectionOption = {
    label: "Select Section",
    value: "select-section",
  };

  return (
    <Select<SectionOption>
      defaultValue={defaultOption} // Adjust the default value as needed
      options={groupedOptions}
      components={{ GroupHeading }}
      styles={{
        groupHeading: (base) => ({
          ...base,
          flex: "1 1",
          color: "white",
          margin: 0,
        }),
      }}
      className="custom-section-select"
    />
  );
};

export default SectionSelect;
