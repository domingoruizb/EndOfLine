import React, { useState } from 'react';
import { Card, CardBody, CardTitle, CardText, Row, Col, ListGroup, ListGroupItem } from 'reactstrap';
import './Social.css';
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
  );
}

export default function Social() {
  const jwt = tokenService.getLocalAccessToken();
  const [stats] = useFetchState(null, '/api/v1/stats', jwt, null, null);
  const [currentPage, setCurrentPage] = useState(1);
  const [rankingsPerPage] = useState(10);

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

  if (!stats || !stats.global) {
    return (
      <main className="social-page" data-testid="social-page">
        <div className="social-container">
          <p style={{textAlign: 'center', color: '#fff'}}>Loading...</p>
        </div>
      </main>
    );
  }

  const global = stats.global;
  const rankings = global.rankings || [];
  const indexOfLastRanking = currentPage * rankingsPerPage;
  const indexOfFirstRanking = indexOfLastRanking - rankingsPerPage;
  const currentRankings = rankings.slice(indexOfFirstRanking, indexOfLastRanking);
  const totalPages = Math.ceil(rankings.length / rankingsPerPage);

  return (
    <main className="social-page" data-testid="social-page">
      <div className="social-container">
        <header className="mb-3">
          <h2 className="mb-1">Social & Global Stats</h2>
        </header>

        <Row className="g-3 stats-row">
          <Col xs={12} sm={6} md={4}>
            <StatCard title="Finished Games (global)">{global.totalFinishedGames}</StatCard>
          </Col>
          <Col xs={12} sm={6} md={4}>
            <StatCard title="Avg Games per Player">{fmtMins(global.avgGamesPerPlayer)}</StatCard>
          </Col>
          <Col xs={12} sm={12} md={4}>
            <StatCard title="Total Duration (hrs)">{fmtHours(global.totalDurationMinutes)}</StatCard>
          </Col>
        </Row>

        <Row className="mt-3">
          <Col xs={12} md={6}>
            <Card className="mb-3 stat-card">
              <CardBody>
                <CardTitle tag="h6" className="stat-title">Games — Global</CardTitle>
                <ListGroup flush>
                  <ListGroupItem className="d-flex justify-content-between align-items-center">
                    <span>Max games by a player</span>
                    <strong>{global.maxGamesByPlayer}</strong>
                  </ListGroupItem>
                  <ListGroupItem className="d-flex justify-content-between align-items-center">
                    <span>Min games by a player</span>
                    <strong>{global.minGamesByPlayer}</strong>
                  </ListGroupItem>
                  <ListGroupItem className="d-flex justify-content-between align-items-center">
                    <span>Favourite skill</span>
                    <strong>{fmtSkill(global.favoriteSkill)}</strong>
                  </ListGroupItem>
                </ListGroup>
              </CardBody>
            </Card>
          </Col>

          <Col xs={12} md={6}>
            <Card className="mb-3 stat-card">
              <CardBody>
                <CardTitle tag="h6" className="stat-title">Durations — Global</CardTitle>
                <ListGroup flush>
                  <ListGroupItem className="d-flex justify-content-between align-items-center">
                    <span>Avg duration per game (min)</span>
                    <strong>{fmtMins(global.avgDurationMinutes)}</strong>
                  </ListGroupItem>
                  <ListGroupItem className="d-flex justify-content-between align-items-center">
                    <span>Max duration (min)</span>
                    <strong>{fmtMins(global.maxDurationMinutes)}</strong>
                  </ListGroupItem>
                  <ListGroupItem className="d-flex justify-content-between align-items-center">
                    <span>Min duration (min)</span>
                    <strong>{fmtMins(global.minDurationMinutes)}</strong>
                  </ListGroupItem>
                </ListGroup>
              </CardBody>
            </Card>
          </Col>
        </Row>

        <Row className="mt-4">
          <Col xs={12}>
            <Card className="stat-card">
              <CardBody>
                <CardTitle tag="h5" className="stat-title mb-3">🏆 Player Rankings</CardTitle>
                <p className="text-muted mb-3" style={{fontSize: '0.9rem'}}>
                  Players ranked by total number of games won
                </p>
                {global.rankings && global.rankings.length > 0 ? (
                  <>
                    <div className="ranking-table">
                      <div className="ranking-header">
                        <span className="rank-col">Rank</span>
                        <span className="player-col">Player</span>
                        <span className="wins-col">Wins</span>
                        <span className="games-col">Games</span>
                        <span className="winrate-col">Win Rate</span>
                      </div>
                      {currentRankings.map((player) => {
                        const winRate = player.gamesPlayed > 0 
                          ? ((player.gamesWon / player.gamesPlayed) * 100).toFixed(1) 
                          : '0.0';
                        const rankClass = player.rank === 1 ? 'rank-first' : 
                                         player.rank === 2 ? 'rank-second' : 
                                         player.rank === 3 ? 'rank-third' : '';
                        
                        return (
                          <div key={player.userId} className={`ranking-row ${rankClass}`}>
                            <span className="rank-col">
                              {player.rank === 1 && '🥇'}
                              {player.rank === 2 && '🥈'}
                              {player.rank === 3 && '🥉'}
                              {player.rank > 3 && `#${player.rank}`}
                            </span>
                            <span className="player-col">{player.username}</span>
                            <span className="wins-col">{player.gamesWon}</span>
                            <span className="games-col">{player.gamesPlayed}</span>
                            <span className="winrate-col">{winRate}%</span>
                          </div>
                        );
                      })}
                    </div>
                    {totalPages > 1 && (
                      <div className="ranking-pagination text-center mt-4">
                        <button
                          onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
                          disabled={currentPage === 1}
                          className="ranking-pagination-button"
                        >
                          Previous
                        </button>
                        <span className="ranking-pagination-info">
                          Page {currentPage} of {totalPages}
                        </span>
                        <button
                          onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
                          disabled={currentPage === totalPages}
                          className="ranking-pagination-button"
                        >
                          Next
                        </button>
                      </div>
                    )}
                  </>
                ) : (
                  <p className="text-muted text-center">No rankings available yet</p>
                )}
              </CardBody>
            </Card>
          </Col>
        </Row>
      </div>
    </main>
  );
}