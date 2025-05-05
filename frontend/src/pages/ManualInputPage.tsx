import React, { useState } from "react";
import {
  Card,
  InputNumber,
  Button,
  Row,
  Col,
  Typography,
  Alert,
  Select,
} from "antd";
import axios from "axios";

const { Title } = Typography;
const { Option } = Select;

const ManualInputPage: React.FC = () => {
  const [lat, setLat] = useState<number | null>(null);
  const [lon, setLon] = useState<number | null>(null);
  const [param, setParam] = useState<string>("");
  const [value, setValue] = useState<number | null>(null);
  const [result, setResult] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async () => {
    try {
      const response = await axios.post(
        "http://localhost:8085/api/scripts/manual",
        null,
        {
          params: {
            lat,
            lon,
            param,
            value,
          },
        }
      );

      setResult(response.data);
      setError(null);
    } catch (err) {
      console.error(err);
      setError("Something went wrong while running the script.");
      setResult(null);
    }
  };

  return (
    <Card title="🛠 Manual Data Injection" style={{ margin: 24 }}>
      <Title level={5}>Enter Data Below</Title>
      <Row gutter={[16, 16]}>
        <Col span={6}>
          <InputNumber
            placeholder="Latitude"
            value={lat ?? undefined}
            onChange={setLat}
            style={{ width: "100%" }}
          />
        </Col>
        <Col span={6}>
          <InputNumber
            placeholder="Longitude"
            value={lon ?? undefined}
            onChange={setLon}
            style={{ width: "100%" }}
          />
        </Col>
        <Col span={6}>
          <Select
            placeholder="Select Pollutant"
            value={param || undefined}
            onChange={(value) => setParam(value)}
            style={{ width: "100%" }}
          >
            <Option value="pm25">PM2.5</Option>
            <Option value="pm10">PM10</Option>
            <Option value="o3">O₃</Option>
            <Option value="so2">SO₂</Option>
            <Option value="no2">NO₂</Option>
            <Option value="co">CO</Option>
          </Select>
        </Col>
        <Col span={6}>
          <InputNumber
            placeholder="Value"
            value={value ?? undefined}
            onChange={setValue}
            style={{ width: "100%" }}
          />
        </Col>
      </Row>

      <Button
        type="primary"
        onClick={handleSubmit}
        disabled={!lat || !lon || !param || !value}
        style={{ marginTop: 16 }}
      >
        Run Script
      </Button>

      {result && (
        <Alert
          message="Script Output"
          description={<pre>{result}</pre>}
          type="success"
          showIcon
          style={{ marginTop: 16 }}
        />
      )}

      {error && (
        <Alert
          message="Error"
          description={error}
          type="error"
          showIcon
          style={{ marginTop: 16 }}
        />
      )}
    </Card>
  );
};

export default ManualInputPage;
