import React, { useState } from 'react';
import { Card, CardBody, CardTitle, Row, Col } from 'reactstrap';
import '../static/css/social/SocialLayout.css';
import '../static/css/social/StatCard.css';
import '../static/css/social/RankingTable.css';
import tokenService from '../services/token.service';
import useFetchState from '../util/useFetchState';
import StatCard from '../components/statsComponents/StatCard';
import RankingTable from '../components/statsComponents/RankingTable';
import GamesGlobalCard from '../components/statsComponents/GamesGlobalCard';
import DurationsGlobalCard from '../components/statsComponents/DurationsGlobalCard';
import { fmtHours, fmtMins } from '../util/formatters';

export default function Social() {
  const jwt = tokenService.getLocalAccessToken();
  const [stats] = useFetchState(null, '/api/v1/stats', jwt, null, null);
  const [currentPage, setCurrentPage] = useState(1);
  const [rankingsPerPage] = useState(10);


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
            <GamesGlobalCard
              maxGamesByPlayer={global.maxGamesByPlayer}
              minGamesByPlayer={global.minGamesByPlayer}
              favoriteSkill={global.favoriteSkill}
            />
          </Col>
          <Col xs={12} md={6}>
            <DurationsGlobalCard
              avgDurationMinutes={global.avgDurationMinutes}
              maxDurationMinutes={global.maxDurationMinutes}
              minDurationMinutes={global.minDurationMinutes}
            />
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
                  <RankingTable
                    rankings={global.rankings}
                    currentRankings={currentRankings}
                    currentPage={currentPage}
                    totalPages={totalPages}
                    setCurrentPage={setCurrentPage}
                  />
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