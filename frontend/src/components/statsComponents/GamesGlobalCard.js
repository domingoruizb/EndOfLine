import React from 'react';
import { Card, CardBody, CardTitle, ListGroup, ListGroupItem } from 'reactstrap';
import { fmtSkill } from '../../util/formatters';

export default function GamesGlobalCard({ maxGamesByPlayer, minGamesByPlayer, favoriteSkill }) {
  return (
    <Card className="mb-3 stat-card">
      <CardBody>
        <CardTitle tag="h6" className="stat-title">Games — Global</CardTitle>
        <ListGroup flush>
          <ListGroupItem className="d-flex justify-content-between align-items-center">
            <span>Max games by a player</span>
            <strong>{maxGamesByPlayer}</strong>
          </ListGroupItem>
          <ListGroupItem className="d-flex justify-content-between align-items-center">
            <span>Min games by a player</span>
            <strong>{minGamesByPlayer}</strong>
          </ListGroupItem>
          <ListGroupItem className="d-flex justify-content-between align-items-center">
            <span>Favourite skill</span>
            <strong>{fmtSkill(favoriteSkill)}</strong>
          </ListGroupItem>
        </ListGroup>
      </CardBody>
    </Card>
  );
}
