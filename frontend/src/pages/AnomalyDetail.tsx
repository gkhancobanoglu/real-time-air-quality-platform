import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Card, Descriptions, Spin, Alert, Button } from "antd";
import dayjs from "dayjs";

interface Anomaly {
  id: number;
  lat: number;
  lon: number;
  timestamp: number;
  aqi: number;
  description: string;
}

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
      <Descriptions bordered column={1}>
        <Descriptions.Item label="Timestamp">
          {dayjs(anomaly.timestamp).format("YYYY-MM-DD HH:mm:ss")}
        </Descriptions.Item>
        <Descriptions.Item label="Latitude">{anomaly.lat}</Descriptions.Item>
        <Descriptions.Item label="Longitude">{anomaly.lon}</Descriptions.Item>
        <Descriptions.Item label="AQI">{anomaly.aqi}</Descriptions.Item>
        <Descriptions.Item label="Description">
          {anomaly.description || "No description available"}
        </Descriptions.Item>
      </Descriptions>
    </Card>
  );
};

export default AnomalyDetail;
