import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  Card,
  Descriptions,
  Spin,
  Alert,
  Button,
  Typography,
  Row,
  Col,
  Tag,
} from "antd";
import dayjs from "dayjs";

const { Title } = Typography;

interface Pollutants {
  pm2_5?: number;
  pm10?: number;
  no2?: number;
  so2?: number;
  o3?: number;
  co?: number;
  nh3?: number;
}

interface Anomaly {
  id: number;
  lat: number;
  lon: number;
  timestamp: number;
  aqi: number;
  description: string;
  pm2_5?: number;
  pm10?: number;
  no2?: number;
  so2?: number;
  o3?: number;
  co?: number;
  nh3?: number;
}

const POLLUTANT_LABELS: { [key in keyof Pollutants]: string } = {
  pm2_5: "PM2.5",
  pm10: "PM10",
  no2: "NO₂",
  so2: "SO₂",
  o3: "O₃",
  co: "CO",
  nh3: "NH₃",
};

const AnomalyDetail: React.FC = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [anomaly, setAnomaly] = useState<Anomaly | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetch(`http://localhost:8082/api/anomalies/${id}`)
      .then((res) => {
        if (!res.ok) throw new Error("Data not found");
        return res.json();
      })
      .then((data) => {
        setAnomaly(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, [id]);

  if (loading) return <Spin style={{ marginTop: 50 }} />;
  if (error || !anomaly)
    return (
      <Alert
        message={`Error: ${error || "Anomaly not found"}`}
        type="error"
        showIcon
      />
    );

  const components: Pollutants = {
    pm2_5: anomaly.pm2_5,
    pm10: anomaly.pm10,
    o3: anomaly.o3,
    no2: anomaly.no2,
    so2: anomaly.so2,
    co: anomaly.co,
    nh3: anomaly.nh3,
  };

  return (
    <Card
      title={`Anomaly Detail - ID: ${anomaly.id}`}
      style={{ margin: 24 }}
      extra={
        <Button type="default" onClick={() => navigate("/anomalies")}>
          ← Back to List
        </Button>
      }
    >
      <Descriptions bordered column={1} size="middle">
        <Descriptions.Item label="Timestamp">
          {dayjs(anomaly.timestamp).format("YYYY-MM-DD HH:mm:ss")}
        </Descriptions.Item>
        <Descriptions.Item label="Latitude">{anomaly.lat}</Descriptions.Item>
        <Descriptions.Item label="Longitude">{anomaly.lon}</Descriptions.Item>
        <Descriptions.Item label="AQI">
          <Tag
            color={
              anomaly.aqi >= 150
                ? "red"
                : anomaly.aqi >= 100
                ? "orange"
                : "green"
            }
          >
            {anomaly.aqi}
          </Tag>
        </Descriptions.Item>
        <Descriptions.Item label="Description">
          {anomaly.description || "No description available"}
        </Descriptions.Item>
      </Descriptions>

      <Title level={5} style={{ marginTop: 32 }}>
        Pollutant Components
      </Title>
      <Row gutter={[16, 16]} justify="start">
        {Object.entries(components).map(
          ([key, value]) =>
            value !== undefined && (
              <Col xs={24} sm={12} md={8} lg={6} key={key}>
                <Card
                  bordered
                  hoverable
                  style={{
                    textAlign: "center",
                    borderRadius: 8,
                    height: 120,
                    display: "flex",
                    flexDirection: "column",
                    justifyContent: "center",
                  }}
                >
                  <Title level={5} style={{ marginBottom: 8 }}>
                    {POLLUTANT_LABELS[key as keyof Pollutants]}
                  </Title>
                  <Tag
                    color="blue"
                    style={{ fontSize: 18, padding: "4px 12px" }}
                  >
                    {value}
                  </Tag>
                </Card>
              </Col>
            )
        )}
      </Row>
    </Card>
  );
};

export default AnomalyDetail;
