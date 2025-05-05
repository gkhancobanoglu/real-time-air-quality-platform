import React, { useState } from "react";
import axios from "axios";
import { Card, InputNumber, Button, Row, Col, Typography, Alert } from "antd";

const { Title, Text } = Typography;

interface PollutionData {
  pm25?: number;
  pm10?: number;
  no2?: number;
  so2?: number;
  o3?: number;
}

const RegionAnalysis: React.FC = () => {
  const [minLat, setMinLat] = useState<number | null>(null);
  const [maxLat, setMaxLat] = useState<number | null>(null);
  const [minLon, setMinLon] = useState<number | null>(null);
  const [maxLon, setMaxLon] = useState<number | null>(null);
  const [data, setData] = useState<PollutionData | null>(null);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async () => {
    try {
      setError(null);
      const response = await axios.get(
        "http://localhost:8083/api/air-quality/pollution/region",
        {
          params: {
            minLat,
            maxLat,
            minLon,
            maxLon,
          },
        }
      );
      setData(response.data);
    } catch (err) {
      console.error("Error fetching regional data", err);
      setError(
        "Failed to fetch region data. Please check the coordinates and try again."
      );
    }
  };

  return (
    <Card title="📊 Regional Pollution Analysis" style={{ margin: 24 }}>
      <Row gutter={[16, 16]}>
        <Col span={6}>
          <InputNumber
            placeholder="Min Lat"
            value={minLat ?? undefined}
            onChange={(val) => setMinLat(val)}
            style={{ width: "100%" }}
          />
        </Col>
        <Col span={6}>
          <InputNumber
            placeholder="Max Lat"
            value={maxLat ?? undefined}
            onChange={(val) => setMaxLat(val)}
            style={{ width: "100%" }}
          />
        </Col>
        <Col span={6}>
          <InputNumber
            placeholder="Min Lon"
            value={minLon ?? undefined}
            onChange={(val) => setMinLon(val)}
            style={{ width: "100%" }}
          />
        </Col>
        <Col span={6}>
          <InputNumber
            placeholder="Max Lon"
            value={maxLon ?? undefined}
            onChange={(val) => setMaxLon(val)}
            style={{ width: "100%" }}
          />
        </Col>
      </Row>

      <Button
        type="primary"
        onClick={handleSubmit}
        style={{ marginTop: 16 }}
        disabled={!minLat || !maxLat || !minLon || !maxLon}
      >
        Get Regional Data
      </Button>

      {error && (
        <Alert
          message={error}
          type="error"
          showIcon
          style={{ marginTop: 16 }}
        />
      )}

      {data && (
        <div style={{ marginTop: 24 }}>
          <Title level={5}>Average Pollution Values</Title>
          {Object.entries(data).map(([key, value]) => (
            <Text key={key}>
              • {key.toUpperCase()}: {value?.toFixed(2)}
              <br />
            </Text>
          ))}
        </div>
      )}
    </Card>
  );
};

export default RegionAnalysis;
