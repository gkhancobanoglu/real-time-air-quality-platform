import React from "react";
import { Routes, Route, Link } from "react-router-dom";
import HomePage from "./pages/HomePage";
import AnomalyList from "./pages/AnomalyList";
import AnomalyDetail from "./pages/AnomalyDetail";
import ManualInputPage from "./pages/ManualInputPage";
import AutoTestPage from "./pages/AutoTestPage";
import HeatMapPage from "./pages/HeatMapPage";
import { Layout, Menu } from "antd";
import "antd/dist/reset.css";

const { Header, Content } = Layout;

const App: React.FC = () => {
  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Header style={{ backgroundColor: "#001529" }}>
        <Menu
          theme="dark"
          mode="horizontal"
          defaultSelectedKeys={["home"]}
          items={[
            { key: "home", label: <Link to="/">Home</Link> },
            { key: "list", label: <Link to="/anomalies">Anomalies</Link> },
            { key: "heatmap", label: <Link to="/heatmap">Heatmap</Link> },
          ]}
        />
      </Header>
      <Content style={{ padding: "24px" }}>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/anomalies" element={<AnomalyList />} />
          <Route path="/anomalies/:id" element={<AnomalyDetail />} />
          <Route path="/manual-input" element={<ManualInputPage />} />
          <Route path="/auto-test" element={<AutoTestPage />} />
          <Route path="/heatmap" element={<HeatMapPage />} />
        </Routes>
      </Content>
    </Layout>
  );
};

export default App;
