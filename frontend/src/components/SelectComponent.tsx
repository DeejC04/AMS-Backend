import React, {SyntheticEvent, useState} from "react";
import {Select, View, Alert} from "@instructure/ui";

// Description of each object in drop down menu
type Option = {
    label: string;
    value: string;
};

type SelectComponentProps = {
    options: Option[];
    onChange: (value: string) => void;
};

const SelectComponent: React.FC<SelectComponentProps> = ({options, onChange}) => {
    const [inputValue, setInputValue] = useState<string>(options[0].label);
    const [isShowingOptions, setIsShowingOptions] = useState<boolean>(false);
    const [highlightedOptionId, setHighlightedOptionId] = useState<string | null>(null);
    const [selectedOptionId, setSelectedOptionId] = useState<string>(options[0].value);
    const [announcement, setAnnouncement] = useState<string | null>(null);

    const getOptionById = (queryId: string) => {
        return options.find((option) => option.value === queryId);
    };

    const handleShowOptions = () => {
        setIsShowingOptions(true);
    };

    const handleHideOptions = () => {
        setIsShowingOptions(false);
        setHighlightedOptionId(null);
    };

    const handleHighlightOption = (event: SyntheticEvent<Element, Event>, data: {
        id?: string
    }) => {
        event.persist();
        const id = data.id;
        if (id) {
            const option = getOptionById(id);
            if (option) {
                setHighlightedOptionId(id);
                setInputValue(event.type === "keydown" ? option.label : inputValue);
                setIsShowingOptions(true);
                setAnnouncement(`${option.label} List expanded. ${options.length} options available.`);
            }
        }
    };

    const handleSelectOption = (event: SyntheticEvent<Element, Event>, data: {
        id?: string
    }) => {
        event.persist();
        const id = data.id;
        if (id) {
            const option = getOptionById(id);
            if (option) {
                setSelectedOptionId(id);
                setInputValue(option.label);
                setIsShowingOptions(false);
                setAnnouncement(`${option.label} selected. List collapsed.`);
                onChange(option.value); // Trigger the parent component's onChange with the selected value
            }
        }
    };


    return (
        <View display="inline-block" padding="small" maxWidth="14rem">
            <Select
                renderLabel="Type of Scan"
                assistiveText="Use arrow keys to navigate options."
                inputValue={inputValue}
                isShowingOptions={isShowingOptions}
                onBlur={handleHideOptions}
                onRequestShowOptions={handleShowOptions}
                onRequestHideOptions={handleHideOptions}
                onRequestHighlightOption={handleHighlightOption}
                onRequestSelectOption={handleSelectOption}
            >
                {options.map((option) => (
                    <Select.Option
                        id={option.value}
                        key={option.value}
                        isHighlighted={option.value === highlightedOptionId}
                        isSelected={option.value === selectedOptionId}
                    >
                        {option.label}
                    </Select.Option>
                ))}
            </Select>
        </View>
    );
};

export default SelectComponent;

