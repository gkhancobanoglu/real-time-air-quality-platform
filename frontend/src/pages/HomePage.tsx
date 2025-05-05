import React from "react";
import { Row, Col, Card, Button, Space } from "antd";
import { Link } from "react-router-dom";
import MapView from "../components/MapView";
import ChartView from "../components/ChartView";
import AnomalyAlert from "../components/AnomalyAlert";
import RegionAnalysis from "../components/RegionAnalysis";

const HomePage: React.FC = () => {
  return (
    <div style={{ padding: "24px" }}>
      {/* 🚀 Script Testing Section */}
      <Card
        bordered={false}
        style={{
          marginBottom: 24,
          background: "#f0f2f5",
          textAlign: "center",
        }}
      >
        <Space size="large">
          <Link to="/manual-input">
            <Button type="primary" size="large">
              🛠 Manual Input
            </Button>
          </Link>
          <Link to="/auto-test">
            <Button type="default" size="large">
              🧪 Auto Test
            </Button>
          </Link>
        </Space>
      </Card>

      {/* 🔔 Real-time Anomalies */}
      <Row gutter={[24, 24]}>
        <Col span={24}>
          <Card title="🚨 Anomaly Alerts" bordered={false}>
            <AnomalyAlert />
          </Card>
        </Col>

        {/* 🗺 Map View */}
        <Col span={24}>
          <Card title="🗺 Air Quality Map" bordered={false}>
            <MapView />
            <Link to="/anomalies">
              <Button type="primary" style={{ marginTop: "16px" }}>
                View All Anomalies
              </Button>
            </Link>
          </Card>
        </Col>

        {/* 📈 Trend Analysis */}
        <Col xs={24} md={12}>
          <Card title="📈 Pollution Trend" bordered={false}>
            <ChartView />
          </Card>
        </Col>

        {/* 🌍 Regional Analysis */}
        <Col xs={24} md={12}>
          <Card title="🌍 Region Analysis" bordered={false}>
            <RegionAnalysis />
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default HomePage;
