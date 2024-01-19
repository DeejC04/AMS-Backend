import Head from "next/head";
import React, { ReactNode } from "react";

type LayoutProps = {
  children: ReactNode;
};

const Layout = ({ children }: LayoutProps) => {
  return (
    <>
      <Head>
        <title>Attendance Management System</title>
        <meta name="description" content="" /> {/* TODO: Add description */}
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <meta name="author" content="Fernando David Nevarez" />{" "}
        {/* add others once they contribute to the project on github */}
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <main>{children}</main>
    </>
  );
};

export default Layout;
