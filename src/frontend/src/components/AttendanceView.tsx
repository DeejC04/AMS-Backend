import React, {
    useEffect,
    useState,
    useContext,
    ChangeEvent,
} from "react";
import {
    Pagination,
    ToggleGroup,
    View,
    TextInput,
    TimeSelect,
    Button,
} from "@instructure/ui";
import axios from "axios";

import type {DateContextType} from "@/contexts/DateContext";
import {DateContext} from "@/contexts/DateContext";
import {SortableTable} from "@/components/SortableTable";
import {CourseInfo} from "./SectionConfig";

// helper functions
import {DateSelect} from "./DateSelect";
import {MultipleSelect} from "./MultipleSelect";
import dayjs from "dayjs";
import SectionSelect from "./SectionSelect";

// types
export type User = {
    id: number,
    name: {
        first: string;
        last: string;
    };
    email: string;
    sid: number;
    type: string;
    time: string;
};
type FilterOpts = {
    beginTime: string | undefined;
    endTime: string | undefined;
    firstName: string | undefined;
    lastName: string | undefined;
    sid: number | undefined;
    typesOfScan: string[] | undefined;
};

const AttendanceView = () => {
    const [attendanceData, setAttendanceData] = useState<User[]>([]);
    const [currentPage, setCurrentPage] = useState<number>(0);
    const [currPageSize, setCurrPageSize] = useState<number>(10);
    const [totalPages, setTotalPages] = useState<number>(0);
    const [pages, setPages] = useState<JSX.Element[]>([]);
    const {currentDate, setCurrentDate} = useContext<DateContextType>(DateContext);
    const [sortBy, setSortBy] = useState<string>("time");
    const [sortType, setSortType] = useState<string>("asc");
    const page_size = [5, 10, 25, 50, 100];
    const courseID = 1234;
    const [currCourse, setCurrCourse] = useState<CourseInfo>();

    // State variables to store the selected values and update the table when "Go" button is clicked
    const [selectedFilters, setSelectedFilters] = useState<FilterOpts>({
        beginTime: undefined,
        endTime: undefined,
        firstName: undefined,
        lastName: undefined,
        sid: undefined,
        typesOfScan: ['ARRIVED', 'LEFT', 'ARRIVED_LATE', 'ARRIVED_INVALID', 'LEFT_INVALID', 'INVALID'],
    });
    const [inputFilters, setInputFilters] = useState<FilterOpts>(selectedFilters);
    // mock section select data
    const groupedOptions = [
        {
          label: "CSE110",
          options: [
            { label: "11086", value: "11086", color: "#FF0000" },
            { label: "11089", value: "11089", color: "#00FF00" },
            { label: "12025", value: "12025", color: "#0000FF" },
          ],
        },
        {
          label: "CSE205",
          options: [
            { label: "24034", value: "24034", color: "#FF0000" },
            { label: "32212", value: "32212", color: "#00FF00" },
            { label: "32224", value: "32224", color: "#0000FF" },
          ],
        },
      ];

    useEffect(() => {
        // fetch start and end time from course config if no time filter
        const fetchCourseConfig = async () => {
            await axios
                .get("https://api.ams-lti.com/courseInfo/" + courseID)
                .then((res) => {
                    const fetchedCourse = res.data as CourseInfo;

                    setCurrCourse(fetchedCourse);
                })
                .catch((err) => {
                    console.error("Error fetching courseInfo:", err);
                });
        };
        // fetch the attendance as per the parameters specified
        const fetchAttendance = async () => {
            console.log(">>>", dayjs(currentDate).format("YYYY-MM-DD"));
            await axios
                .get("https://api.ams-lti.com/attendance", {
                    headers: {
                        "Access-Control-Allow-Origin": "https://api.ams-lti.com",
                    },
                    params: {
                        room: "COOR170",
                        date: dayjs(currentDate).format("YYYY-MM-DD"),
                        startTime: selectedFilters?.beginTime,
                        endTime: selectedFilters?.endTime,
                        page: currentPage,
                        size: currPageSize,
                        sid: selectedFilters?.sid,
                        sortBy: sortBy,
                        sortType: sortType,
                        types: selectedFilters?.typesOfScan?.join(', '),

                    },
                })
                .then((res) => {
                    //setting attendance data
                    setAttendanceData(res.data);
                    // retrieving totalPages from response header
                    let total = parseInt(res.headers["total-pages"]);
                    setTotalPages(total);
                    // creating the pages
                    let p = Array.from(Array(total)).map((v, i) => (
                        <Pagination.Page
                            key={i}
                            onClick={() => setCurrentPage(i)}
                            current={i === currentPage}
                        >
                            {i + 1}
                        </Pagination.Page>
                    ));
                    setPages(p);
                })
                .catch((err) => {
                    console.error("Error fetching attendance:", err);
                    setAttendanceData([]);
                });
        };
        fetchAttendance();
    }, [currentDate, currentPage, currPageSize, selectedFilters, sortBy, sortType]);

    const handleSelectPageSize = (e: ChangeEvent<HTMLSelectElement>) => {
        setCurrPageSize(parseInt(e.target.value));
        setCurrentPage(0);
    };

    // retrieve time string from ISO Date string
    const retrieveTimeFromISODate = (dateVal: string | undefined): string => {
        if (!dateVal) {
            return "";
        } else {
            const formattedDate = new Date(dateVal).toLocaleString();

            const [date, time, meridian] = formattedDate.split(" ");
            let [hours, minutes] = time.split(":").map((p) => parseInt(p));
            if (meridian === "PM" && hours !== 12) {
                hours += 12;
            } else if (meridian === "AM" && hours === 12) {
                hours = 0;
            }

            return (
                hours.toString().padStart(2, "0") +
                ":" +
                minutes.toString().padStart(2, "0") +
                ":00"
            );
        }
    };

    // Filter the attendanceData based on user input values
    const handleSearchButtonClick = () => {
        // Updating state variables for handling sorting

        if (inputFilters) {
            const filterVals = {
                beginTime: retrieveTimeFromISODate(inputFilters.beginTime),
                endTime: retrieveTimeFromISODate(inputFilters.endTime),
                firstName: inputFilters.firstName,
                lastName: inputFilters.lastName,
                sid: inputFilters.sid,
                typesOfScan: inputFilters.typesOfScan,
            }
            setSelectedFilters(filterVals);
            setCurrentPage(0);
        }
    };

    // Clearing the sorting
    const handleClearButtonClick = () => {
        // resetting the values for all the state variables
        const filterVals = {
            beginTime: undefined, 
            endTime: undefined,
            firstName: undefined,
            lastName: undefined,
            sid: undefined,
            typesOfScan: ['ARRIVED', 'LEFT', 'ARRIVED_LATE', 'ARRIVED_INVALID', 'LEFT_INVALID', 'INVALID'],
        }
        setSelectedFilters(filterVals);
        setInputFilters(filterVals);
        setCurrentPage(0);
    };

    // Handling sort by and sort type for the sorting by updating the component state variables
    const handleSorting = (selectedSortState: { sortBy: string, ascending: boolean }) => {
        const {sortBy, ascending} = selectedSortState
        setSortBy(sortBy)
        setSortType(ascending ? 'asc' : 'desc')
    }

    // Handling select type for the multi select
    const handleSelectTypeId = (selectedTypeId: string[]) => {
        setInputFilters((prevData: FilterOpts) => {
            if (prevData) {
                return {...prevData, typesOfScan: selectedTypeId};
            }
            return prevData;
        })
    };

    // @ts-ignore
    return (
        <div className="mx-32">
            <SectionSelect groupedOptions={groupedOptions} />
            <DateSelect
                currentDate={currentDate}
                setCurrentDate={setCurrentDate}
                disabled={false}
            ></DateSelect>
            <div>
                <h1 className="text-center font-medium text-3xl">
                    Attendance Table View
                </h1>
                <br/>
                <ToggleGroup
                    size="small"
                    toggleLabel="Column-specific filtering options"
                    summary="Filtering"
                >
                    <View display="inline-block" padding="small" maxWidth="15rem">
                        <TimeSelect
                            renderLabel="Begin Time"
                            id="beginTime"
                            placeholder="Select a Begin Time"
                            step={5}
                            onChange={(e, {value}) =>
                                setInputFilters((prevData: FilterOpts) => {
                                    if (prevData) {
                                        return {...prevData, beginTime: value};
                                    }
                                    return prevData;
                                })
                            }
                            onInputChange={(e, value, isoValue) =>
                                setInputFilters((prevData: FilterOpts) => {
                                    if (prevData) {
                                        return {...prevData, beginTime: isoValue};
                                    }
                                    return prevData;
                                })
                            }
                            value={inputFilters?.beginTime ? inputFilters?.beginTime : ""}
                            allowNonStepInput
                        />
                    </View>

                    <View display="inline-block" padding="small" maxWidth="15rem">
                        <TimeSelect
                            renderLabel="End Time"
                            id="endTime"
                            placeholder="Select an End Time"
                            step={5}
                            onChange={(e, {value}) =>
                                setInputFilters((prevData: FilterOpts) => {
                                    if (prevData) {
                                        return {...prevData, endTime: value};
                                    }
                                    return prevData;
                                })
                            }
                            onInputChange={(e, value, isoValue) =>
                                setInputFilters((prevData: FilterOpts) => {
                                    if (prevData) {
                                        return {...prevData, endTime: isoValue};
                                    }
                                    return prevData;
                                })
                            }
                            value={inputFilters?.endTime ? inputFilters?.endTime : ""}
                            allowNonStepInput
                        />
                    </View>

                    <View display="inline-block" padding="small" width="25rem">
                        <TextInput
                            renderLabel="Last Name"
                            placeholder="Enter exact last name to search"
                            onChange={(event, value) =>
                                setInputFilters((prevData: FilterOpts) => {
                                    if (prevData) {
                                        return {...prevData, lastName: value};
                                    }
                                    return prevData;
                                })
                            }
                            value={inputFilters?.lastName ? inputFilters?.lastName : ""}
                        />
                    </View>

                    <View display="inline-block" padding="small" width="25rem">
                        <TextInput
                            renderLabel="First Name"
                            placeholder="Enter exact first name to search"
                            onChange={(event, value) =>
                                setInputFilters((prevData: FilterOpts) => {
                                    if (prevData) {
                                        return {...prevData, firstName: value};
                                    }
                                    return prevData;
                                })
                            }
                            value={inputFilters?.firstName ? inputFilters?.firstName : ""}
                        />
                    </View>

                    <View display="inline-block" padding="small" maxWidth="15rem">
                        <TextInput
                            renderLabel="ID #"
                            placeholder="Enter ASU ID"
                            onChange={(event, value) =>
                                setInputFilters((prevData: FilterOpts) => {
                                    if (prevData) {
                                        return {...prevData, sid: parseInt(value)};
                                    }
                                    return prevData;
                                })
                            }
                            value={inputFilters?.sid ? (inputFilters?.sid).toString() : ""}
                        />
                    </View>

                    <br></br>
                    <View display="inline-block" padding="small" width="54rem">
                        <MultipleSelect
                            options={[
                                {id: "ARRIVED", label: "Arrived", value: "ARRIVED"},
                                {id: "LEFT", label: "Left", value: "LEFT"},
                                {id: "ARRIVED_LATE", label: "Arrived Late", value: "ARRIVED_LATE"},
                                {
                                    id: "ARRIVED_INVALID",
                                    label: "Arrived Invalid",
                                    value: "ARRIVED_INVALID",
                                },
                                {id: "LEFT_INVALID", label: "Left Invalid", value: "LEFT_INVALID"},
                                {id: "INVALID", label: "Invalid", value: "INVALID"},
                            ]}
                            handleSelectTypeId={handleSelectTypeId}
                            selectedTypeIDs={inputFilters?.typesOfScan || []}
                        />
                    </View>
                    <button
                        className="filter-btn"
                        onClick={handleSearchButtonClick}
                    >
                        Search
                    </button>

                    <button
                        className="filter-btn filter-clr"
                        onClick={handleClearButtonClick}
                    >
                        Clear
                    </button>

                </ToggleGroup>
                <br/>
                {attendanceData.length > 0 ? (
                    <SortableTable
                        layout="auto"
                        caption="Sortable table with attendance records"
                        headers={[
                            {id: "time", text: "Time"},
                            {id: "last", text: "Last"},
                            {id: "first", text: "First"},
                            {id: "sid", text: "ID #"},
                            {id: "type", text: "Type"},
                        ]}
                        rows={attendanceData}
                        handleSorting={handleSorting}
                        sortBy={sortBy}
                    />
                ) : (
                    <p>No attendance records found.</p>
                )}
            </div>
            <div className="page-size-box">
                <label>Page Size:</label>
                <select
                    name="tourneyStyle"
                    value={currPageSize}
                    onChange={handleSelectPageSize}
                >
                    {page_size.map((p) => (
                        <option key={p} value={p}>
                            {" "}
                            {p}{" "}
                        </option>
                    ))}
                </select>
            </div>
            <div>
                <Pagination
                    as="nav"
                    margin="large small small"
                    variant="compact"
                    labelNext="Next Page"
                    labelPrev="Previous Page"
                    labelFirst="First Page"
                    labelLast="Last Page"
                    withFirstAndLastButton={false}
                    showDisabledButtons={true}
                >
                    {pages}
                </Pagination>
            </div>
        </div>
    );
};

export default AttendanceView;
