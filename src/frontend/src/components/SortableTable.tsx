import React from "react"
import Icon from "@/components/Icon"
import {
    Table,
    ToggleGroup,
    RadioInputGroup,
    ScreenReaderContent,
    RadioInput,
    FormField,
    Flex,
    Responsive,
    View,
    Alert
} from "@instructure/ui"
import {User} from "./AttendanceView"

interface Header {
    id: string;
    text: string;
    // width: string | null;
}

type IDType = {
    [K in keyof User]: K extends 'name' ? never : K;
  }[keyof User];
  
interface SortableTableProps {
    layout: string;
    caption: string;
    headers: Header[];
    rows: User[];
    handleSorting: (selectedSortState: SortState) => void;
    sortBy: string; // Add this if it's a required prop
}
  
interface SortState {
    sortBy: string;
    ascending: boolean;
}
  
interface SortableTableState {
    sortBy: string;
    ascending: boolean;
}
  

export class SortableTable extends React.Component<SortableTableProps, SortableTableState> {
    constructor(props: SortableTableProps) {
        super(props)

        this.state = {
            sortBy: props.sortBy,
            ascending: true
        }
    }

    handleSort = (event: React.MouseEvent | React.SyntheticEvent<Element, Event>, { id }: { id: string }) => {
        const {sortBy, ascending} = this.state

        this.setState({
            sortBy: id,
            ascending: !ascending, // connected to the UI for arrow direction
        })

    }

    componentDidUpdate(prevProps: any, prevState: any) {
        // checking parent state of sortBy variable
        if (prevState.sortBy !== this.state.sortBy ||
            prevState.ascending !== this.state.ascending) {
            this.updateParentState();
        }
    }

    updateParentState = () => {
        const {handleSorting} = this.props;
        handleSorting(this.state);
    }

    renderHeaderRow(direction: string) {
        const {headers} = this.props
        const {sortBy} = this.state

        return (
            <Table.Row>
                {(headers || []).map(({id, text}) => (
                    <Table.ColHeader
                        key={id}
                        id={id}
                        // width={width}
                        {...(direction && {
                            stackedSortByLabel: text,
                            onRequestSort: this.handleSort,
                            sortDirection: id === sortBy ? direction as "ascending" | "none" | "descending": 'none'
                        })}
                    >
                        {text}
                    </Table.ColHeader>
                ))}
            </Table.Row>
        )
    }

    render() {
        const {caption, headers, rows} = this.props
        const {sortBy, ascending} = this.state
        const direction = ascending ? 'ascending' : 'descending'
        const sortedRows = [...(rows || [])]
        
        return (
            <Responsive
                query={{
                    small: {maxWidth: '40rem'},
                    large: {minWidth: '41rem'},
                }}
                props={{
                    small: {layout: 'stacked'},
                    large: {layout: 'auto'},
                }}
            >
                {(props) => (
                    <div>

                        <Table
                            caption={`${caption}: sorted by ${sortBy} in ${direction} order`}
                            {...props}
                        >
                            <Table.Head renderSortLabel="Sort by">
                                {this.renderHeaderRow(direction)}
                            </Table.Head>
                            <Table.Body>
                                {sortedRows.map((row) => (
                                    <Table.Row key={row.id}>
                                        {headers.map(({id}) => (
                                            <Table.Cell key={id}>
                                                {id === "type" ? <Icon status={row[id]}/> : row[id as IDType]}
                                            </Table.Cell>
                                        ))}
                                    </Table.Row>
                                ))}
                            </Table.Body>
                        </Table>
                    </div>
                )}
            </Responsive>
        )
    }
}

