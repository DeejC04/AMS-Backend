import {Button, CloseButton, Flex, Heading, Tray, View} from "@instructure/ui";
import ThresholdBox from "./ThresholdBox";
import SpecConfigBox from "./SpecConfigBox";
import SectionConfig from "./SectionConfig";
import React from "react";


interface ConfigTrayProps {
}

interface ConfigTrayState {
  open: boolean;
}


export class ConfigTray extends React.Component<ConfigTrayProps, ConfigTrayState> {
  constructor(props: ConfigTrayProps) {
    super(props);
    this.state = {
      open: false,
    };
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
          <div className="expand-label" style={{display: 'inline-block'}}>Date Specific Time Config</div>
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
          this.setState({open: false});
        }}
        shouldCloseOnDocumentClick={true}
        transitionExit={true}
        size="regular"
        placement="end"
      >
        <View as="div" padding="medium">
          {this.renderCloseButton()}
          <SpecConfigBox/>
          <ThresholdBox/>
          <SectionConfig/>
        </View>
      </Tray>
    ));
    MemoizedTray.displayName = 'MemoizedTray';

    return (
      <div className="time-config-btn">
        <Button
          onClick={() => {
            this.setState({open: true});
          }}
          // ref={(c) => (this._showButton = c)}
        >
          Show Time Config
        </Button>
        <MemoizedTray/>
      </div>
    );
  }
}
