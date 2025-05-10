import React, { useState, useEffect } from "react";
import { Card } from "antd";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
} from "recharts";

type ChartData = {
  time: string;
  pm25: number;
  pm10: number;
};

const ChartView: React.FC = () => {
  const [data, setData] = useState<ChartData[]>([]);

  useEffect(() => {
    fetch(`${process.env.REACT_APP_STORAGE_API}/api/air-quality`)
      .then((res) => res.json())
      .then((rawData) => {
        const formatted = rawData.map((item: any) => ({
          time: new Date(item.timestamp).toLocaleTimeString(),
          pm25: item.pm25 ?? 0,
          pm10: item.pm10 ?? 0,
        }));
        setData(formatted);
      })
      .catch((err) => {
        console.error("Failed to fetch chart data", err);
      });
  }, []);

  return (
    <Card title="📈 Air Pollution Trend" style={{ margin: 24 }}>
      <LineChart width={800} height={400} data={data}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="time" />
        <YAxis />
        <Tooltip />
        <Legend />
        <Line type="monotone" dataKey="pm25" stroke="#8884d8" name="PM2.5" />
        <Line type="monotone" dataKey="pm10" stroke="#82ca9d" name="PM10" />
      </LineChart>
    </Card>
  );
};

export default ChartView;
