import React, { useEffect, useState } from "react";
import { Card } from "antd";
import {
  MapContainer,
  TileLayer,
  Marker,
  Popup,
  Tooltip,
  useMap,
} from "react-leaflet";
import "leaflet/dist/leaflet.css";
import "leaflet.heat";
import L from "leaflet";
import dayjs from "dayjs";

interface AnomalyPoint {
  id: number;
  lat: number;
  lon: number;
  aqi: number;
  description: string;
  timestamp: string;
}

const HeatLayer: React.FC<{ points: [number, number, number][] }> = ({
  points,
}) => {
  const map = useMap();

  useEffect(() => {
    if (!(window as any).L?.heatLayer) {
      console.error("❌ leaflet.heat is not loaded.");
      return;
    }

    const heatLayer = (window as any).L.heatLayer(points, {
      radius: 30,
      blur: 20,
      maxZoom: 15,
      gradient: {
        0.2: "#6aff6a",
        0.4: "#ffe66a",
        0.6: "#ff8c42",
        0.8: "#ff4e4e",
        1.0: "#990000",
      },
    }).addTo(map);

    return () => {
      map.removeLayer(heatLayer);
    };
  }, [map, points]);

  return null;
};

const redIcon = new L.Icon({
  iconUrl: "https://maps.google.com/mapfiles/ms/icons/red-dot.png",
  iconSize: [32, 32],
  iconAnchor: [16, 32],
});

const MapView: React.FC = () => {
  const [livePoints, setLivePoints] = useState<[number, number, number][]>([]);
  const [markerPoints, setMarkerPoints] = useState<AnomalyPoint[]>([]);

  useEffect(() => {
    fetch(`${process.env.REACT_APP_ANOMALY_API}/api/anomalies`)
      .then((res) => res.json())
      .then((data: AnomalyPoint[]) => {
        setLivePoints(
          data.map((item) => [item.lat, item.lon, Math.min(item.aqi / 500, 1)])
        );
        setMarkerPoints(data);
      });

    const eventSource = new EventSource(
      `${process.env.REACT_APP_NOTIFICATION_API}/api/notifications/stream`
    );

    eventSource.onmessage = (event) => {
      try {
        const data: AnomalyPoint = JSON.parse(event.data);
        if (!data.lat || !data.lon || !data.aqi) return;

        setMarkerPoints((prev) =>
          prev.some((p) => p.id === data.id) ? prev : [...prev, data]
        );

        setLivePoints((prev) => [
          ...prev.slice(-99),
          [data.lat, data.lon, Math.min(data.aqi / 500, 1)],
        ]);
      } catch (err) {
        console.error("❌ Invalid SSE Data:", err);
      }
    };

    eventSource.onerror = () => eventSource.close();

    return () => eventSource.close();
  }, []);

  return (
    <Card title="🌍 Real-Time Air Quality Map" style={{ marginBottom: 24 }}>
      <MapContainer
        center={[41.015, 28.979]}
        zoom={11}
        style={{ height: "500px", width: "100%" }}
        worldCopyJump={false}
        maxBounds={[
          [-85, -180],
          [85, 180],
        ]}
        maxBoundsViscosity={1.0}
      >
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution="&copy; OpenStreetMap contributors"
        />
        <HeatLayer points={livePoints} />

        {markerPoints.map((point) => (
          <Marker
            key={point.id}
            position={[point.lat, point.lon]}
            icon={redIcon}
          >
            <Tooltip direction="top" offset={[0, -20]} opacity={0.9}>
              AQI: {point.aqi}
            </Tooltip>
            <Popup>
              <div style={{ minWidth: 220 }}>
                <h4>📍 Anomaly Detail</h4>
                <p>
                  <strong>Timestamp:</strong>{" "}
                  {dayjs(point.timestamp).format("YYYY-MM-DD HH:mm:ss")}
                </p>
                <p>
                  <strong>Latitude:</strong> {point.lat}
                </p>
                <p>
                  <strong>Longitude:</strong> {point.lon}
                </p>
                <p>
                  <strong>AQI:</strong> {point.aqi}
                </p>
                <p>
                  <strong>Description:</strong> {point.description}
                </p>
              </div>
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </Card>
  );
};

export default MapView;
