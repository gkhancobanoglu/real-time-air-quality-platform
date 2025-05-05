import React, { useState } from "react";
import { Card, Button, Typography, Alert, Row, Col, InputNumber } from "antd";
import axios from "axios";

const { Title, Text } = Typography;

const AutoTestPage: React.FC = () => {
  const [result, setResult] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [duration, setDuration] = useState<number | null>(30);
  const [rate, setRate] = useState<number | null>(2);
  const [anomalyChance, setAnomalyChance] = useState<number | null>(30);

  const handleRunTest = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await axios.post(
        "http://localhost:8085/api/scripts/autotest",
        null,
        {
          params: {
            duration,
            rate,
            anomalyChance,
          },
        }
      );
      setResult("Auto-test script started.");
    } catch (err) {
      setError("Auto test script failed.");
      setResult(null);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card title="🧪 Auto Test Simulation" style={{ margin: 24 }}>
      <Title level={5}>Run an automatic pollution anomaly test</Title>
      <Text type="secondary">
        This will randomly generate air pollution data and simulate anomalies
        for testing the system.
      </Text>

      <Row gutter={[16, 16]} style={{ marginTop: 16 }}>
        <Col span={8}>
          <InputNumber
            min={1}
            max={300}
            value={duration ?? undefined}
            onChange={(value) => setDuration(value)}
            addonBefore="Duration"
            addonAfter="sec"
            style={{ width: "100%" }}
          />
        </Col>
        <Col span={8}>
          <InputNumber
            min={1}
            max={10}
            value={rate ?? undefined}
            onChange={(value) => setRate(value)}
            addonBefore="Rate"
            addonAfter="req/s"
            style={{ width: "100%" }}
          />
        </Col>
        <Col span={8}>
          <InputNumber
            min={0}
            max={100}
            value={anomalyChance ?? undefined}
            onChange={(value) => setAnomalyChance(value)}
            addonBefore="Anomaly"
            addonAfter="%"
            style={{ width: "100%" }}
          />
        </Col>
      </Row>

      <Button
        type="primary"
        onClick={handleRunTest}
        loading={loading}
        style={{ marginTop: 16 }}
        disabled={duration === null || rate === null || anomalyChance === null}
      >
        Run Auto Test
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

export default AutoTestPage;
