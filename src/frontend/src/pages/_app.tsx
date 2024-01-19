// components
import Layout from "@/components/pages/Layout";

// contexts
import DateProvider from "@/contexts/DateContext";

// styles
import "@/styles/globals.css";

// types
import type { AppProps } from "next/app";

const App = ({ Component, pageProps }: AppProps) => {
  return (
    <DateProvider>
      <Layout>
        <Component {...pageProps} />
      </Layout>
    </DateProvider>
  );
};

export default App;
