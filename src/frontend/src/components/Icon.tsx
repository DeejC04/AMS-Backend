import { BiCheckCircle, BiError } from "react-icons/bi";
import { IconType } from "react-icons";
import React, { useMemo, useState, useCallback, Component } from "react";
import { Tooltip } from "@instructure/ui";

import { IconPublishLine, IconWarningSolid, IconQuestionLine,
    IconQuizStatsLowLine, IconQuizStatsHighLine,  } from '@instructure/ui-icons'
import type { SVGIconProps } from '@instructure/ui-svg-images';

type IconManagerProps = {
  status: string;
};

// we are making the color prop optional because we want to be able to set the color of the icon using the status prop. But allow the user to override the color if they want to.
const IconManager = ({ status }: IconManagerProps) => {
  status = status.toLowerCase();

  // by using useMemo, we can prevent the iconMap and iconColorMap objects from being recreated on every render
  // TODO: Make it so we aren't using `any` as the value type here
  const iconMap: { [key: string]: any } = useMemo(
    () => ({
        arrived: IconQuizStatsLowLine,
        arrived_late: IconQuizStatsLowLine,
        arrived_invalid: IconQuizStatsLowLine,
        left: IconQuizStatsHighLine,
        left_invalid: IconQuizStatsHighLine,
        invalid: IconWarningSolid,
    }),
    []
  );

  const iconColorMap: { [key: string]: string } = useMemo(
    () => ({
      arrived: "green",
      arrived_late: "gold",
      arrived_invalid: "red",
      left: "green",
      left_invalid: "red",
      invalid: "red",
    }),
    []
  );

  const Icon = iconMap[status] || IconQuestionLine;
  const iconColor = iconColorMap[status] || iconColorMap.default;

  return (
    <>
      <Tooltip renderTip={
        status.split('_').map((s) => s.charAt(0).toUpperCase()+s.slice(1)).join(' ')
      }>
        <Icon
          style={{ color: iconColor, stroke: iconColor, strokeWidth: '75px', transform: 'scale(1.25)' }}
          // onMouseEnter={handleShowStatus}
          // onMouseLeave={handleShowStatus}
          // className="flex justify-center items-center"
        />
      </Tooltip>
      {/* {showStatus && <>{status.charAt(0).toUpperCase() + status.slice(1)}</>} */}
    </>
  );
};

export default React.memo(IconManager); // by exporting the component wrapped in React.memo, we can prevent unnecessary re-renders
