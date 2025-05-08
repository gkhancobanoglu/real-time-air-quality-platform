import React, { useState } from "react";
import axios from "axios";
import {
  Card,
  InputNumber,
  Button,
  Row,
  Col,
  Alert,
  Descriptions,
  Tag,
} from "antd";

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
    if (
      minLat === null ||
      maxLat === null ||
      minLon === null ||
      maxLon === null
    ) {
      setError("Please enter all coordinates.");
      return;
    }

    if (minLat >= maxLat || minLon >= maxLon) {
      setError(
        "Invalid coordinates. Make sure min values are less than max values."
      );
      return;
    }

    try {
      setError(null);
      setData(null);

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

      if (response.status !== 200) {
        setError("Failed to fetch region data. Please check the coordinates.");
        return;
      }

      setData(response.data);
    } catch (err) {
      console.error("Error fetching regional data", err);
      setError("Failed to fetch region data. Please try again later.");
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
        <Descriptions
          title="Average Pollution Values"
          bordered
          column={1}
          style={{ marginTop: 24 }}
        >
          {Object.entries(data).map(([key, value]) => (
            <Descriptions.Item label={key.toUpperCase()} key={key}>
              <Tag color="blue">{value?.toFixed(2)}</Tag>
            </Descriptions.Item>
          ))}
        </Descriptions>
      )}
    </Card>
  );
};

export default RegionAnalysis;
