import React, { useEffect, useState } from "react";
import { MapContainer, TileLayer, useMap } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";
import "leaflet.heat";
import { Card, Typography, Spin, Alert } from "antd";

const { Title } = Typography;

interface Anomaly {
  id: number;
  lat: number;
  lon: number;
  aqi: number;
}

const HeatLayer: React.FC<{ points: Anomaly[] }> = ({ points }) => {
  const map = useMap();

  useEffect(() => {
    const heatData = points.map((p) => [p.lat, p.lon, p.aqi / 500]); // normalize AQI (0–1 arası)
    const heatLayer = (L as any).heatLayer(heatData, {
      radius: 25,
      blur: 15,
      maxZoom: 6,
      minOpacity: 0.5,
      gradient: {
        0.1: "blue",
        0.3: "lime",
        0.6: "orange",
        0.8: "red",
      },
    });

    heatLayer.addTo(map);
    return () => {
      map.removeLayer(heatLayer);
    };
  }, [map, points]);

  return null;
};

const HeatMapPage: React.FC = () => {
  const [anomalies, setAnomalies] = useState<Anomaly[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetch(`${process.env.REACT_APP_ANOMALY_API}/api/anomalies`)
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch anomalies");
        return res.json();
      })
      .then((data) => {
        setAnomalies(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, []);

  if (loading) return <Spin style={{ marginTop: 50 }} />;
  if (error)
    return (
      <Alert
        message="Error fetching anomaly data"
        description={error}
        type="error"
        showIcon
        style={{ margin: 24 }}
      />
    );

  return (
    <Card style={{ margin: 24 }}>
      <Title level={4}>🌍 Global Air Pollution Heatmap</Title>
      <MapContainer
        center={[20, 0]}
        zoom={2}
        style={{ height: "70vh", width: "100%", marginTop: 16 }}
      >
        <TileLayer
          attribution="&copy; OpenStreetMap"
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        <HeatLayer points={anomalies} />
      </MapContainer>
    </Card>
  );
};

export default HeatMapPage;
