import React from "react";
import { Layout } from "antd";
import { Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage.tsx";
import AnomalyList from "./pages/AnomalyList.tsx";
import AnomalyDetail from "./pages/AnomalyDetail.tsx"; // 💥 Muhtemelen bu eksik

const { Header, Content, Footer } = Layout;

function App() {
  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Header style={{ color: "white" }}>
        Real-Time Air Quality Monitoring
      </Header>
      <Content style={{ padding: "24px" }}>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/anomalies" element={<AnomalyList />} />
          <Route path="/anomalies/:id" element={<AnomalyDetail />} />
        </Routes>
      </Content>
      <Footer style={{ textAlign: "center" }}>
        Kartaca Hava Kalitesi Projesi ©2025
      </Footer>
    </Layout>
  );
}

export default App;
