import React from 'react';
import { Card, CardBody, CardTitle, CardText, Row, Col, ListGroup, ListGroupItem } from 'reactstrap';
import './UserStats.css';
// their statistics, including games played, durations, wins 
const testStats = {
  totalGames: 124,
  totalWins: 98,
  totalDuration: 26,
}

function StatCard({ title, children }) {
  return (
    <Card className="mb-3 stat-card">
      <CardBody>
        <CardTitle tag="h6" className="stat-title">{title}</CardTitle>
        <CardText className="stat-number">{children}</CardText>
      </CardBody>
    </Card>
  )
}

export default function UserStats() {
  const stats = testStats;

  return (
    <main className="user-stats-page" data-testid="user-stats-page">
      <div className="user-stats-container">
        <header className="mb-3">
          <h2 className="mb-1">User Statistics</h2>
        </header>

        <Row className="g-3 stats-row">
          <Col xs={12} sm={6} md={4}>
            <StatCard title="Games">{stats.totalGames}</StatCard>
          </Col>
          <Col xs={12} sm={6} md={4}>
            <StatCard title="Wins">{stats.totalWins}</StatCard>
          </Col>
          <Col xs={12} sm={12} md={4}>
            <StatCard title="Duration (hrs)">{stats.totalDuration}</StatCard>
          </Col>
        </Row>
      </div>
    </main>
  )
}
