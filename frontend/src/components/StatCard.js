import React from 'react';
import { Card, CardBody, CardTitle, CardText } from 'reactstrap';

export default function StatCard({ title, children }) {
  return (
    <Card className="mb-3 stat-card">
      <CardBody>
        <CardTitle tag="h6" className="stat-title">{title}</CardTitle>
        <CardText className="stat-number">{children}</CardText>
      </CardBody>
    </Card>
  );
}
