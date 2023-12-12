import React from "react";
import {Select, Tag} from "@instructure/ui";
import {ViewProps} from "@instructure/ui-view";

interface Option {
    id: string;
    label: string;
    value: string;
}
  
interface MultipleSelectProps {
    options: Option[];
    handleSelectTypeId: (selectedTypeId: string[]) => void;
    selectedTypeIDs: string[];
}
  
interface MultipleSelectState {
    inputValue: string;
    isShowingOptions: boolean;
    highlightedOptionId: string | null;
    selectedOptionId: string[];
    filteredOptions: Option[];
    announcement: string | null;
}
interface SelectProps {
    id?: string;
    assistiveText?: string;
    interaction?: "enabled" | "disabled" | "readonly";
    isInline?: boolean;
    visibleOptionsCount?: number;
    optionsMaxHeight?: string;
    optionsMaxWidth?: string;
    inputValue?: string;
    onInputChange?: (event: React.ChangeEvent<HTMLInputElement>, value: string) => void;
    listRef?: (listElement: (HTMLUListElement | null)) => void;
    children?: React.ReactNode;
}
export class MultipleSelect extends React.Component<MultipleSelectProps, MultipleSelectState> {
    state = {
        inputValue: '',
        isShowingOptions: false,
        highlightedOptionId: null,
        selectedOptionId: this.props.selectedTypeIDs,
        filteredOptions: this.props.options,
        announcement: null
    }
    inputRef: any;

    getOptionById(queryId: string ) {
        return this.props.options.find(({id}) => id === queryId)
    }

    updateParentState = () => {
        const { handleSelectTypeId } = this.props;
        handleSelectTypeId(this.state.selectedOptionId);
    }

    componentDidUpdate(prevProps: any, prevState: any) {
        if (prevState.selectedOptionId !== this.state.selectedOptionId) {
          this.updateParentState();
        }
        if (prevProps.selectedTypeIDs !== this.props.selectedTypeIDs) {
            this.setState({ selectedOptionId: this.props.selectedTypeIDs });
          }
      }

    getOptionsChangedMessage(newOptions: any) {
        let message = newOptions.length !== this.state.filteredOptions.length
            ? `${newOptions.length} options available.` // options changed, announce new total
            : null // options haven't changed, don't announce
        if (message && newOptions.length > 0) {
            // options still available
            if (this.state.highlightedOptionId !== newOptions[0].id) {
                // highlighted option hasn't been announced
                const placeholderForOption:Option | undefined = this.getOptionById(newOptions[0].id)
                if (placeholderForOption){
                    const option = this.getOptionById(placeholderForOption.id)?.label || 'Unknown';
                    message = `${option}. ${message}`
                }
            }
        }
        return message
    }

    filterOptions = (value: string) => {
        const {selectedOptionId} = this.state
        return this.props.options.filter(option => (option.label.toLowerCase().startsWith(value.toLowerCase())
        ))
    }

    matchValue() {
        const {
            filteredOptions,
            inputValue,
            highlightedOptionId,
            selectedOptionId
        } = this.state

        // an option matching user input exists
        if (filteredOptions.length === 1) {
            const onlyOption = filteredOptions[0]
            // automatically select the matching option
            if (onlyOption.label.toLowerCase() === inputValue.toLowerCase()) {
                return {
                    inputValue: '',
                    selectedOptionId: [...selectedOptionId, onlyOption.id],
                    filteredOptions: this.filterOptions('')
                }
            }
        }
        // input value is from highlighted option, not user input
        // clear input, reset options
        if (highlightedOptionId) {
            //@ts-ignore
            if (inputValue === this.getOptionById(highlightedOptionId).label) {
                return {
                    inputValue: '',
                    filteredOptions: this.filterOptions('')
                }
            }
        }
    }

    handleShowOptions = (event:React.SyntheticEvent<Element, Event>) => {
        this.setState({isShowingOptions: true})
    }

    handleHideOptions = (event: React.SyntheticEvent<Element, Event>) => {
        //@ts-ignore
        this.setState({
            isShowingOptions: false,
            ...this.matchValue()
        })
    }

    handleBlur = (event: React.SyntheticEvent<Element, Event>) => {
        this.setState({
            highlightedOptionId: null
        })
    }

    handleHighlightOption =(event:React.SyntheticEvent<Element, Event>, {id} : { id?: string | undefined}) => {
        event.persist()
        if (id){
            const option = this.getOptionById(id)
            if (!option) return // prevent highlighting empty option
            this.setState((state) => ({
                highlightedOptionId: id || null,
                inputValue: event.type === 'keydown' ? option.label : state.inputValue,
                announcement: option.label
            }))
        }
    }

    handleSelectOption = (event:React.SyntheticEvent<Element, Event>, {id} : { id?: string | undefined }) => {
        if (id){

            const option = this.getOptionById(id)
            if (!option) return // prevent selecting of empty option
            this.setState((state) => ({
                selectedOptionId: [...state.selectedOptionId, id],
                highlightedOptionId: null,
                filteredOptions: this.filterOptions(''),
                inputValue: '',
                isShowingOptions: false,
                announcement: `${option.label} selected. List collapsed.`
            }))
        }
    }

    handleInputChange = ((event:React.ChangeEvent<HTMLInputElement>) => {
        const value = event.target.value
        const newOptions = this.filterOptions(value)
        this.setState({
            inputValue: value,
            filteredOptions: newOptions,
            highlightedOptionId: newOptions.length > 0 ? newOptions[0].id : null,
            isShowingOptions: true,
            announcement: this.getOptionsChangedMessage(newOptions)
        })
    })

    handleKeyDown = (event: React.KeyboardEvent<SelectProps>) => {
        const {selectedOptionId, inputValue} = this.state
        if (event.key === 'Backspace') {
            // when backspace key is pressed
            if (inputValue === '' && selectedOptionId.length > 0) {
                // remove last selected option, if input has no entered text
                this.setState((state) => ({
                    highlightedOptionId: null,
                    selectedOptionId: state.selectedOptionId.slice(0, -1)
                }))
            }
        }
    }

    // remove a selected option tag
    dismissTag(e: React.MouseEvent<ViewProps, MouseEvent>, tag: string) {
        // prevent closing of list
        e.stopPropagation()
        e.preventDefault()

        const newSelection = this.state.selectedOptionId.filter((id) => id !== tag)
        this.setState({
            selectedOptionId: newSelection,
            highlightedOptionId: null
        }, () => {
            this.inputRef.focus()
        })
    }

    // render tags when multiple options are selected
    
    renderTags() {
        const {selectedOptionId} = this.state
        return selectedOptionId.map((id, index) => (
            <Tag
                dismissible
                key={id}
                title={`Remove ${this.getOptionById(id)?.label}`}
                text={this.getOptionById(id)?.label}
                margin={index > 0 ? 'xxx-small 0 xxx-small xx-small' : 'xxx-small 0'}
                onClick={(e) => this.dismissTag(e, id)}
            />
        ))
    }

    render() {
        const {
            inputValue,
            isShowingOptions,
            highlightedOptionId,
            selectedOptionId,
            filteredOptions,
        } = this.state
        return (
            <div>
                <Select
                    renderLabel="Type of Scan"
                    assistiveText="Type or use arrow keys to navigate options. Multiple selections allowed."
                    inputValue={inputValue}
                    isShowingOptions={isShowingOptions}
                    inputRef={(el: HTMLInputElement | null) => this.inputRef = el}
                    onBlur={this.handleBlur}
                    onInputChange={this.handleInputChange}
                    onRequestShowOptions={this.handleShowOptions}
                    onRequestHideOptions={this.handleHideOptions}
                    onRequestHighlightOption={(event: React.SyntheticEvent<Element, Event>, data: { id?: string | undefined }) => {
                        this.handleHighlightOption(event, data);
                    }}
                    onRequestSelectOption={(event: React.SyntheticEvent<Element, Event>, data: { id?: string | undefined }) => {
                        this.handleSelectOption(event, data);
                    }}
                    onKeyDown={(event) => this.handleKeyDown(event)}
                    renderBeforeInput={selectedOptionId.length > 0 ? this.renderTags() : null}
                >
                    {filteredOptions.length > 0 ? filteredOptions.map((option, index) => {
                        if (selectedOptionId.indexOf(option.id) === -1) {
                            return (
                                <Select.Option
                                    id={option.id}
                                    key={option.id}
                                    isHighlighted={option.id === highlightedOptionId}
                                    isSelected={selectedOptionId.includes(option.id)}
                                >
                                    {option.label}
                                </Select.Option>
                            )
                        }
                    }) : (
                        <Select.Option
                            id="empty-option"
                            key="empty-option"
                        >
                            ---
                        </Select.Option>
                    )}
                </Select>
            </div>
        )
    }
}

