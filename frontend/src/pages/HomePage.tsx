import React from "react";
import { Row, Col, Card, Button } from "antd";
import { Link } from "react-router-dom";
import MapView from "../components/MapView.tsx";
import ChartView from "../components/ChartView.tsx";
import AnomalyAlert from "../components/AnomalyAlert.tsx";
import RegionAnalysis from "../components/RegionAnalysis.tsx";

const HomePage: React.FC = () => {
  return (
    <div style={{ padding: "24px" }}>
      <Row gutter={[24, 24]}>
        <Col span={24}>
          <Card title="Anomaly Alerts" bordered={false}>
            <AnomalyAlert />
          </Card>
        </Col>

        <Col span={24}>
          <Card title="Air Quality Map" bordered={false}>
            <MapView />
            {/* Anomalies Link butonu */}
            <Link to="/anomalies">
              <Button type="primary" style={{ marginTop: "16px" }}>
                View All Anomalies
              </Button>
            </Link>
          </Card>
        </Col>

        <Col xs={24} md={12}>
          <Card title="Pollution Trend" bordered={false}>
            <ChartView />
          </Card>
        </Col>

        <Col xs={24} md={12}>
          <Card title="Region Analysis" bordered={false}>
            <RegionAnalysis />
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default HomePage;
