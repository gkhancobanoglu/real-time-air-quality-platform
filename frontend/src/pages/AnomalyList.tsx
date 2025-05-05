import React, { useEffect, useState } from "react";
import { Table, Typography, Tag, Button } from "antd";
import { useNavigate } from "react-router-dom";

const { Title } = Typography;

interface Anomaly {
  id: number;
  lat: number;
  lon: number;
  timestamp: number;
  aqi: number;
  description: string;
}

const AnomalyList: React.FC = () => {
  const [data, setData] = useState<Anomaly[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    fetch("http://localhost:8082/api/anomalies")
      .then((res) => res.json())
      .then((data) => {
        setData(data);
        setLoading(false);
      })
      .catch(() => setLoading(false));
  }, []);

  const columns = [
    {
      title: "Timestamp",
      dataIndex: "timestamp",
      key: "timestamp",
      render: (timestamp: number) => new Date(timestamp).toLocaleString(),
    },
    {
      title: "Location",
      key: "location",
      render: (_: any, record: Anomaly) =>
        `${record.lat.toFixed(4)}, ${record.lon.toFixed(4)}`,
    },
    {
      title: "AQI",
      dataIndex: "aqi",
      key: "aqi",
      render: (aqi: number) => (
        <Tag color={aqi >= 3 ? "red" : "green"}>{aqi}</Tag>
      ),
    },
    {
      title: "Description",
      dataIndex: "description",
      key: "description",
    },
  ];

  return (
    <div style={{ padding: "24px" }}>
      <Button type="default" onClick={() => navigate("/")}>
        ← Back to Home
      </Button>

      <Title level={2} style={{ marginTop: 16 }}>
        Detected Anomalies
      </Title>

      <Table
        rowKey="id"
        columns={columns}
        dataSource={data}
        loading={loading}
        onRow={(record) => ({
          onClick: () => navigate(`/anomalies/${record.id}`),
          style: { cursor: "pointer" },
        })}
      />
    </div>
  );
};

export default AnomalyList;
