import React, { useState } from "react";
import { MdOutlineNetworkWifi } from "react-icons/md";

interface PairOptionProps {
  name: string;
  status: string;
}

const PairOption: React.FC<PairOptionProps> = (props) => {
  const { name, status } = props;
  const [currentStatus, setCurrentStatus] = useState(status);

  const handleStatusChange = () => {
    // // Make a POST request to update the status
    // fetch("your-api-endpoint", {
    //   method: "POST",
    //   headers: {
    //     "Content-Type": "application/json",
    //   },
    //   body: JSON.stringify({
    //     name,
    //     status: currentStatus === "Unpaired"? "Paired": "Unpaired", // Set the new status value
    //   }),
    // })
    //   .then((response) => response.json())
    //   .then((data) => {
    //     // Update the component state after successful POST request
    //     setCurrentStatus(data.status);
    //   })
    //   .catch((error) => {
    //     console.error("Error updating status:", error);
    //   });

    if (currentStatus === "Paired") {
      setCurrentStatus("Unpaired");
    } else if (currentStatus === "Unpaired") {
      setCurrentStatus("Paired");
    }
  };

  return (
    <div className="pair-list-items">
      <div style={{ display: 'flex', flexDirection: 'row', alignItems: 'flex-start'}}>
        <MdOutlineNetworkWifi size={30} style={{ marginRight: '10px' }}/>
        <div>
          <h3>{name}</h3>
          <p
            className={
              currentStatus === "Paired"
                ? "status-paired"
                : currentStatus === "Unpaired"
                ? "status-unpaired"
                : "status-maintenance"
            }
          >
            {currentStatus}
          </p>
        </div>
      </div>
      <button
        onClick={handleStatusChange}
        disabled={currentStatus === "Maintenance" ? true : false}
      >
        {currentStatus === "Maintenance"
          ? "Disabled"
          : currentStatus === "Unpaired"
          ? "Pair"
          : "Unpair"}
      </button>
    </div>
  );
};

export default PairOption;
