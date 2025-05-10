import React, { useEffect, useState } from "react";
import { Card, Collapse, Badge, Pagination, Typography } from "antd";
import dayjs from "dayjs";

const { Panel } = Collapse;
const { Text } = Typography;

interface Anomaly {
  id: number;
  lat: number;
  lon: number;
  aqi: number;
  description: string;
  timestamp: number;
}

const AnomalyAlert: React.FC = () => {
  const [alerts, setAlerts] = useState<Anomaly[]>([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize, setPageSize] = useState(5);

  useEffect(() => {
    fetch(`${process.env.REACT_APP_ANOMALY_API}/api/anomalies`)
      .then((res) => res.json())
      .then((data: Anomaly[]) => {
        setAlerts(data.reverse());
      });

    const eventSource = new EventSource(
      `${process.env.REACT_APP_NOTIFICATION_API}/api/notifications/stream`
    );

    eventSource.onmessage = (event) => {
      try {
        const data: Anomaly = JSON.parse(event.data);
        if (!data.id || !data.lat || !data.lon || !data.timestamp) return;

        setAlerts((prev) => {
          const exists = prev.find((item) => item.id === data.id);
          if (exists) return prev;
          return [data, ...prev];
        });
      } catch (err) {
        console.error("❌ Invalid JSON in SSE:", event.data);
      }
    };

    return () => {
      eventSource.close();
    };
  }, []);

  const start = (currentPage - 1) * pageSize;
  const paginated = alerts.slice(start, start + pageSize);

  return (
    <Card
      title="🚨 Real-time Anomaly Alerts"
      bordered
      style={{ marginBottom: 24 }}
    >
      <Collapse accordion>
        {paginated.map((item, index) => (
          <Panel
            key={`${item.id}-${index}`}
            header={
              <span>
                <Badge status="processing" /> AQI: {item.aqi}
              </span>
            }
          >
            <p>
              <Text type="secondary">
                {dayjs(item.timestamp).format("YYYY-MM-DD HH:mm:ss")}
              </Text>
            </p>
            <p>
              <strong>Description:</strong> {item.description}
              <br />
              <strong>Latitude:</strong> {item.lat}
              <br />
              <strong>Longitude:</strong> {item.lon}
            </p>
          </Panel>
        ))}
      </Collapse>

      <Pagination
        current={currentPage}
        pageSize={pageSize}
        total={alerts.length}
        onChange={(page, size) => {
          setCurrentPage(page);
          setPageSize(size);
        }}
        showSizeChanger
        pageSizeOptions={["5", "10", "20"]}
        style={{ marginTop: 16, textAlign: "center" }}
      />
    </Card>
  );
};

export default AnomalyAlert;
