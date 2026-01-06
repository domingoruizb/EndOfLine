// TODO: Probably should be refactored
// Possibly extract StatCard to a general components folder since Social also uses it
import React from 'react';
import { Card, CardBody, CardTitle, CardText, Row, Col, ListGroup, ListGroupItem } from 'reactstrap';
import '../static/css/stats/UserStats.css';
import tokenService from '../services/token.service';
import useFetchState from '../util/useFetchState';

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
  const jwt = tokenService.getLocalAccessToken();
  const [stats] = useFetchState(null, '/api/v1/stats', jwt, null, null);

  const fmtHours = (mins) => (mins / 60).toFixed(1);
  const fmtMins = (n) => Math.round(n);
  const fmtSkill = (s) => {
    if (!s) return '—';
    try {
      return String(s)
        .replace(/_/g, ' ')
        .toLowerCase()
        .replace(/\b\w/g, (c) => c.toUpperCase());
    } catch {
      return s;
    }
  };

  if (!stats || !stats.user) {
    return (
      <main className="user-stats-page" data-testid="user-stats-page">
        <div className="user-stats-container">
          <p style={{textAlign: 'center', color: '#fff'}}>Loading...</p>
        </div>
      </main>
    );
  }

  const user = stats.user;

  return (
    <main className="user-stats-page" data-testid="user-stats-page">
      <div className="user-stats-container">
        <header className="mb-3">
          <h2 className="mb-1">My Statistics</h2>
        </header>

        <Row className="g-3 stats-row">
          <Col xs={12} sm={6} md={3}>
            <StatCard title="My Games Played">{user.gamesPlayed}</StatCard>
          </Col>
          <Col xs={12} sm={6} md={3}>
            <StatCard title="My Wins">{user.wins || 0}</StatCard>
          </Col>
          <Col xs={12} sm={6} md={3}>
            <StatCard title="My Losses">{user.losses || 0}</StatCard>
          </Col>
          <Col xs={12} sm={6} md={3}>
            <StatCard title="My Avg Duration (min)">{fmtMins(user.avgDurationMinutes)}</StatCard>
          </Col>
        </Row>


        <Row className="mt-3">
          <Col xs={12} md={6}>
            <Card className="mb-3 stat-card">
              <CardBody>
                <CardTitle tag="h6" className="stat-title">Games — Detailed</CardTitle>
                <ListGroup flush>
                  <ListGroupItem className="d-flex justify-content-between align-items-center">
                    <span>My games played</span>
                    <strong>{user.gamesPlayed}</strong>
                  </ListGroupItem>
                  <ListGroupItem className="d-flex justify-content-between align-items-center">
                    <span>My favourite skill</span>
                    <strong>{fmtSkill(user.favoriteSkill)}</strong>
                  </ListGroupItem>
                </ListGroup>
              </CardBody>
            </Card>
          </Col>
          <Col xs={12} md={6}>
            <Card className="mb-3 stat-card">
              <CardBody>
                <CardTitle tag="h6" className="stat-title">Durations — Detailed</CardTitle>
                <ListGroup flush>
                  <ListGroupItem className="d-flex justify-content-between align-items-center">
                    <span>My total duration (hrs)</span>
                    <strong>{fmtHours(user.totalDurationMinutes)}</strong>
                  </ListGroupItem>
                  <ListGroupItem className="d-flex justify-content-between align-items-center">
                    <span>My avg duration (min)</span>
                    <strong>{fmtMins(user.avgDurationMinutes)}</strong>
                  </ListGroupItem>
                  <ListGroupItem className="d-flex justify-content-between align-items-center">
                    <span>My max duration (min)</span>
                    <strong>{fmtMins(user.maxDurationMinutes)}</strong>
                  </ListGroupItem>
                  <ListGroupItem className="d-flex justify-content-between align-items-center">
                    <span>My min duration (min)</span>
                    <strong>{fmtMins(user.minDurationMinutes)}</strong>
                  </ListGroupItem>
                </ListGroup>
              </CardBody>
            </Card>
          </Col>
        </Row>
      </div>
    </main>
  );
}
