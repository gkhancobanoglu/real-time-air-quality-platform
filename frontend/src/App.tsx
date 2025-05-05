import React from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import HomePage from "./pages/HomePage";
import AnomalyList from "./pages/AnomalyList";
import AnomalyDetail from "./pages/AnomalyDetail";
import { Layout, Menu } from "antd";
import "antd/dist/reset.css";

const { Header, Content } = Layout;

const App: React.FC = () => {
  return (
    <Router>
      <Layout style={{ minHeight: "100vh" }}>
        <Header style={{ backgroundColor: "#001529" }}>
          <Menu
            theme="dark"
            mode="horizontal"
            defaultSelectedKeys={["home"]}
            items={[
              {
                key: "home",
                label: <Link to="/">Home</Link>, // Link ile yönlendirme
              },
              {
                key: "list",
                label: <Link to="/anomalies">Anomalies</Link>, // Link ile yönlendirme
              },
            ]}
          />
        </Header>
        <Content style={{ padding: "24px" }}>
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/anomalies" element={<AnomalyList />} />
            <Route path="/anomalies/:id" element={<AnomalyDetail />} />
          </Routes>
        </Content>
      </Layout>
    </Router>
  );
};

export default App;
