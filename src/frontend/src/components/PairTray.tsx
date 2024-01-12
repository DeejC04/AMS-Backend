import {
  Button,
  CloseButton,
  Flex,
  Heading,
  Tray,
  View,
} from "@instructure/ui";
import PairOption from "./PairOption";
import React from "react";

interface PairTrayProps {}

interface PairTrayState {
  open: boolean;
  scannerData: { name: string; status: string }[];
}

export class PairTray extends React.Component<PairTrayProps, PairTrayState> {
  constructor(props: PairTrayProps) {
    super(props);
    this.state = {
      open: false,
      scannerData: [],
    };
  }

  componentDidMount() {
    // // Make a GET request to fetch scanner data
    // fetch('your-api-endpoint')
    //   .then(response => response.json())
    //   .then(data => {
    //     // Update the component state with the fetched data
    //     this.setState({
    //       scannerData: data,
    //     });
    //   })
    //   .catch(error => {
    //     console.error('Error fetching scanner data:', error);
    //   });
    const mockScannerData = [
      { name: "Scanner 1", status: "Paired" },
      { name: "Scanner 2", status: "Unpaired" },
      { name: "Scanner 3", status: "Maintenance" },
      // Add more mock data as needed
    ];
    this.setState({
      scannerData: mockScannerData,
    });
    console.log("Fetched");
  }

  hideTray = () => {
    this.setState({
      open: false,
    });
  };

  renderCloseButton() {
    return (
      <Flex>
        <Flex.Item shouldGrow shouldShrink>
          <div className="expand-label" style={{ display: "inline-block" }}>
            Available AMS Scanners
          </div>
        </Flex.Item>
        <Flex.Item>
          <CloseButton
            placement="end"
            offset="small"
            screenReaderLabel="Close"
            onClick={this.hideTray}
          />
        </Flex.Item>
      </Flex>
    );
  }

  render() {
    const MemoizedTray = React.memo(() => (
      <Tray
        label="Tray Example"
        open={this.state.open}
        onDismiss={() => {
          this.setState({ open: false });
        }}
        shouldCloseOnDocumentClick={true}
        transitionExit={true}
        size="regular"
        placement="end"
      >
        <View as="div" padding="medium">
          {this.renderCloseButton()}

          {/* Add the scanner list component, fetch the list above, and populate */}
          <div>
            {this.state.scannerData.map((scanner, index) => (
              <PairOption
                key={index}
                name={scanner.name}
                status={scanner.status}
              />
            ))}
          </div>
        </View>
      </Tray>
    ));
    MemoizedTray.displayName = "MemoizedTray";

    return (
      <div className="Pair-config-btn">
        <Button
          onClick={() => {
            this.setState({ open: true });
          }}
          // ref={(c) => (this._showButton = c)}
        >
          Show Scanners
        </Button>
        <MemoizedTray />
      </div>
    );
  }
}
