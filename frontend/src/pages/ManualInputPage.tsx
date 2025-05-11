import React, { useState } from "react";
import {
  Card,
  InputNumber,
  Button,
  Row,
  Col,
  Typography,
  Alert,
  Form,
} from "antd";
import axios from "axios";

const { Title } = Typography;

const ManualInputPage: React.FC = () => {
  const [form] = Form.useForm();
  const [result, setResult] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (values: any) => {
    setLoading(true);
    try {
      const params = {
        lat: values.lat,
        lon: values.lon,
        co: values.co,
        no: values.no,
        no2: values.no2,
        o3: values.o3,
        so2: values.so2,
        pm25: values.pm25,
        pm10: values.pm10,
        nh3: values.nh3,
      };

      const response = await axios.post(
        `${process.env.REACT_APP_SCRIPT_RUNNER_API}/api/scripts/manual`,
        null,
        { params }
      );

      setResult(`✅ Script triggered. Exit code/output:\n${response.data}`);
      setError(null);
    } catch (err) {
      console.error(err);
      setError("❌ Failed to trigger the manual script.");
      setResult(null);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card title="🛠 Manual Data Injection" style={{ margin: 24 }}>
      <Title level={5}>
        Manually enter pollutant data to simulate an event
      </Title>
      <Form
        layout="vertical"
        form={form}
        onFinish={handleSubmit}
        autoComplete="off"
      >
        <Row gutter={16}>
          <Col span={6}>
            <Form.Item name="lat" label="Latitude" rules={[{ required: true }]}>
              <InputNumber style={{ width: "100%" }} />
            </Form.Item>
          </Col>
          <Col span={6}>
            <Form.Item
              name="lon"
              label="Longitude"
              rules={[{ required: true }]}
            >
              <InputNumber style={{ width: "100%" }} />
            </Form.Item>
          </Col>
          <Col span={6}>
            <Form.Item name="co" label="CO" rules={[{ required: true }]}>
              <InputNumber style={{ width: "100%" }} />
            </Form.Item>
          </Col>
          <Col span={6}>
            <Form.Item name="no" label="NO" rules={[{ required: true }]}>
              <InputNumber style={{ width: "100%" }} />
            </Form.Item>
          </Col>
        </Row>

        <Row gutter={16}>
          <Col span={6}>
            <Form.Item name="no2" label="NO₂" rules={[{ required: true }]}>
              <InputNumber style={{ width: "100%" }} />
            </Form.Item>
          </Col>
          <Col span={6}>
            <Form.Item name="o3" label="O₃" rules={[{ required: true }]}>
              <InputNumber style={{ width: "100%" }} />
            </Form.Item>
          </Col>
          <Col span={6}>
            <Form.Item name="so2" label="SO₂" rules={[{ required: true }]}>
              <InputNumber style={{ width: "100%" }} />
            </Form.Item>
          </Col>
          <Col span={6}>
            <Form.Item name="pm25" label="PM2.5" rules={[{ required: true }]}>
              <InputNumber style={{ width: "100%" }} />
            </Form.Item>
          </Col>
        </Row>

        <Row gutter={16}>
          <Col span={6}>
            <Form.Item name="pm10" label="PM10" rules={[{ required: true }]}>
              <InputNumber style={{ width: "100%" }} />
            </Form.Item>
          </Col>
          <Col span={6}>
            <Form.Item name="nh3" label="NH₃" rules={[{ required: true }]}>
              <InputNumber style={{ width: "100%" }} />
            </Form.Item>
          </Col>
        </Row>

        <Form.Item>
          <Button type="primary" htmlType="submit" loading={loading}>
            Send Data
          </Button>
        </Form.Item>
      </Form>

      {result && (
        <Alert
          message="Success"
          description={<pre>{result}</pre>}
          type="success"
          showIcon
        />
      )}
      {error && (
        <Alert message="Error" description={error} type="error" showIcon />
      )}
    </Card>
  );
};

export default ManualInputPage;
