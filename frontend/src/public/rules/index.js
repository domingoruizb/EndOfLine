import React from "react";
import { Container } from "reactstrap";
import "../../static/css/auth/authPage.css";

export default function RulesPage() {
    return (
        <Container className="auth-page-container" style={{ padding: "2rem 1rem" }}>
            <h1 className="text-center">Rules and Guidelines</h1>
            <div style={{ maxWidth: 900, margin: "1.5rem auto" }}>
                <p>
                    <b>END OF LINE</b> is a game for 1–2 players* <br />
                    Below are the rules for the VERSUS mode for 2 players.
                    If you already know these rules, refer directly to the ANNEX, which describes different game modes and setup for more players.
                    <br />
                    <i>*Each EOL box contains cards for 2 players. EOL allows up to 8 players if you have multiple copies of the game.</i>
                </p>

                <h3>OBJECTIVE</h3>
                <p>
                    In END OF LINE, your single goal is simple:
                    <b>Cut your opponent’s line before they cut yours.</b>
                </p>

                <h3>COMPONENTS (54 CARDS)</h3>
                <p>
                    2 Energy Cards<br />
                    2 Start Cards<br />
                    50 Line Cards
                </p>

                <h3>PLAY AREA</h3>
                <p>
                    In a 2-player VERSUS game, the play area is a 7x7 grid of Line Cards, whose edges are orthogonally connected.
                    EOL does not use a physical board — players build the grid as they place their Line Cards, without exceeding the 7x7 limit.
                    The side edges connect orthogonally (Top–Bottom; Left–Right). These boundaries are determined by the players’ placed Line Cards.
                </p>

                <h3>CARD TYPES</h3>
                <p>
                    <b>START CARD</b><br />
                    This card begins your line. Place it on the table as shown in the setup section.
                </p>
                <p>
                    <b>ENERGY CARD</b><br />
                    Represents your energy. Its orientation shows how much you have left.
                    You start with 3 Energy Points.
                </p>
                <p>
                    <b>LINE CARDS</b><br />
                    Used to form your path or line. There are 8 types of Line Cards.
                    Each Line Card is divided into 3 sections:
                    <ul>
                        <li>Entry – where your line comes in.</li>
                        <li>Exit(s) – arrow(s) showing where your line continues.</li>
                        <li>Initiative Number – determines turn order each round.</li>
                    </ul>
                    Note: When placing a Line Card, ensure its Entry arrow matches one of the Exit arrows from the previous card. Each Line Card has only 1 Entry but may have multiple Exits.
                </p>

                <h3>SETUP</h3>
                <p>
                    Choose a color and take all corresponding cards.<br />
                    Shuffle your Line Cards and place them face-down as a draw deck.<br />
                    Place your Energy Card next to it with the number 3 facing up.<br />
                    Finally, place your Start Card as shown in the setup diagram.
                </p>

                <h3>START OF THE GAME</h3>
                <p>
                    To determine who goes first:
                    <ul>
                        <li>Each player reveals the top card from their deck.</li>
                        <li>Compare Initiative numbers — the player with the lower number goes first.</li>
                        <li>In case of a tie, reveal again until resolved.</li>
                    </ul>
                    Then, both players return revealed cards to their decks, shuffle, and draw 5 cards for their starting hands.
                    <br />
                    Note: If unhappy with your starting hand, you may redraw once — shuffle all 5 cards back and draw 5 new ones. You must keep the new hand.
                </p>

                <h3>ROUND STRUCTURE</h3>
                <p>
                    After the first round, for each new round:
                    <ul>
                        <li>Compare the Initiative of the last Line Card placed by each player.</li>
                        <li>The lowest number plays first.</li>
                        <li>If tied, compare the previous card, and so on.</li>
                        <li>If you reach the Start Card, use the same turn order as Round 1.</li>
                    </ul>
                    Each round has 3 phases:
                    <ol>
                        <li>Turn Order Phase</li>
                        <li>Draw Phase</li>
                        <li>Action Phase</li>
                    </ol>
                </p>

                <h3>DRAW PHASE</h3>
                <p>
                    Each player draws from their deck until they have 5 cards in hand.
                </p>

                <h3>ACTION PHASE</h3>
                <p>
                    Players take turns placing Line Cards on the table, continuing their line.
                    <ul>
                        <li>Each card must connect properly (Entry → Exit) with the last card played.</li>
                        <li>In Round 1: Each player places 1 Line Card after their Start Card.</li>
                        <li>In later rounds: Each player must place 2 Line Cards per round.</li>
                    </ul>
                    After both players have placed their cards, the round ends.
                </p>

                <h3>ENERGY USE</h3>
                <p>
                    You have 3 Energy Points, tracked with your Energy Card.
                    Energy can be spent starting from Round 3 (max 1 point per round).
                    During your turn in the Action Phase, you can spend 1 Energy Point to use one of these effects:
                    <ul>
                        <li><b>BOOST (ACELERÓN)</b>: Place 3 Line Cards this round (instead of 2).</li>
                        <li><b>BRAKE (FRENAZO)</b>: Place 1 Line Card this round (instead of 2).</li>
                        <li><b>REVERSE (MARCHA ATRÁS)</b>: Continue your line from a free Exit on your second-to-last Line Card (instead of the last).</li>
                        <li><b>EXTRA FUEL (GAS EXTRA)</b>: Draw 1 extra Line Card.</li>
                    </ul>
                    To activate an effect, rotate your Energy Card 90° clockwise to show it’s been used.
                    Manage your energy wisely — it’s limited!
                </p>

                <h3>END OF THE GAME</h3>
                <p>
                    If a player cannot place any Line Card during their Action Phase, their line is cut and they lose.
                    Their opponent wins the game.
                </p>
                
                <h3>ANNEX: PLAY AREA SIZES</h3>
                <p>
                    <b>Players</b> - <b>Play Area</b><br />
                    2 - 7x7<br />
                    3 - 7x7<br />
                    4 - 9x9<br />
                    5 - 9x9<br />
                    6 - 11x11<br />
                    7 - 11x11<br />
                    8 - 13x13
                </p>
            </div>
        </Container>
    );
}
