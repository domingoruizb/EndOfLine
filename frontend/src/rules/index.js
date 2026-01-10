import { Container } from "reactstrap";
import "../static/css/rules/rulesPage.css";

export default function RulesPage({ embed = false }) {
    const outerClass = embed ? "rules-outer-embed" : "rules-outer";
    const outerStyle = !embed
        ? {
            backgroundImage: `linear-gradient(rgba(0,0,0,0.2), rgba(0,0,0,0.2)), url(/images/background.png)`,
            backgroundSize: "cover",
            backgroundPosition: "center",
            backgroundRepeat: "no-repeat"
        }
        : undefined;

    return (
        <div className={outerClass} style={outerStyle}>
            <Container
                className={`info-container rules-container ${embed ? 'rules-container-embed' : ''}`}
            >
                <h1 className="text-center rules-title">
                    Rules and Guidelines
                </h1>
                <section className="rules-section">
                    <p style={{ fontSize: "1.15rem", lineHeight: 1.7 }}>
                        <span className="rules-highlight">
                            END OF LINE
                        </span>{" "}
                        is a game for <b>1–2 players*</b>
                        <br />
                        <span style={{ color: "#B1D12D" }}>
                            Below are the rules for the <b>VERSUS</b> mode for 2 players.
                        </span>
                        <br />
                        If you already know these rules, refer directly to the <span style={{ color: "#FE5B02" }}>ANNEX</span>, which describes different game modes and setup for more players.
                        <br />
                        <i className="rules-note">
                            *Each EOL box contains cards for 2 players. EOL allows up to 8 players if you have multiple copies of the game.
                        </i>
                    </p>
                </section>

                <section className="rules-section">
                    <h3 className="rules-section-title">OBJECTIVE</h3>
                    <p style={{ fontSize: "1.1rem" }}>
                        In END OF LINE, your single goal is simple:
                        <br />
                        <span className="rules-objective">
                            Cut your opponent’s line before they cut yours.
                        </span>
                    </p>
                </section>

                <section className="rules-section">
                    <h3 className="rules-section-title">COMPONENTS <span className="rules-section-title-orange">(54 CARDS)</span></h3>
                    <div className="rules-playarea">
                        <img
                            src='/images/rules.png'
                            alt="Rule illustration"
                            className="rules-playarea-img"
                        />
                    </div>
                    <ul className="rules-list">
                        <li>- <span className="rules-card-type">2 Energy Cards</span></li>
                        <li>- <span className="rules-card-type">2 Start Cards</span></li>
                        <li>- <span className="rules-card-type">50 Line Cards</span></li>
                    </ul>
                </section>

                <section className="rules-section">
                    <h3 className="rules-section-title">PLAY AREA</h3>
                    <p>
                        In a 2-player VERSUS game, the play area is a <span className="rules-card-type">7x7 grid</span> of Line Cards, whose edges are orthogonally connected.
                        <br />
                        <span className="rules-card-type">EOL does not use a physical board</span> — players build the grid as they place their Line Cards, without exceeding the 7x7 limit.
                        <br />
                        The side edges connect orthogonally (Top–Bottom; Left–Right).
                        <span className="rules-section-title-orange"> These boundaries are determined by the players’ placed Line Cards.</span>
                    </p>
                </section>

                <section className="rules-section">
                    <h3 className="rules-section-title">CARD TYPES</h3>
                    <div style={{ marginBottom: "1rem" }}>
                        <span className="rules-card-type">START CARD</span>
                        <br />
                        <span className="rules-card-type-desc">
                            This card begins your line. Place it on the table as shown in the setup section.
                        </span>
                    </div>
                    <div style={{ marginBottom: "1rem" }}>
                        <span className="rules-card-type">ENERGY CARD</span>
                        <br />
                        <span className="rules-card-type-desc">
                            Represents your energy. Its orientation shows how much you have left.
                            You start with <span className="rules-section-title-orange rules-card-type">3 Energy Points</span>.
                        </span>
                    </div>
                    <div>
                        <span className="rules-card-type">LINE CARDS</span>
                        <br />
                        <span className="rules-card-type-desc">
                            Used to form your path or line. There are <b>8 types</b> of Line Cards.
                            Each Line Card is divided into 3 sections:
                        </span>
                        <ul className="rules-card-type-list">
                            <li>
                                <span className="rules-section-title-orange rules-card-type">Entry</span> – where your line comes in.
                            </li>
                            <li>
                                <span className="rules-section-title-orange rules-card-type">Exit(s)</span> – arrow(s) showing where your line continues.
                            </li>
                            <li>
                                <span className="rules-section-title-orange rules-card-type">Initiative Number</span> – determines turn order each round.
                            </li>
                        </ul>
                        <span className="rules-note">
                            Note: When placing a Line Card, ensure its Entry arrow matches one of the Exit arrows from the previous card. Each Line Card has only 1 Entry but may have multiple Exits.
                        </span>
                    </div>
                </section>

                <section className="rules-section">
                    <h3 className="rules-section-title">SETUP</h3>
                    <ul className="rules-card-type-list">
                        <li>Choose a color and take all corresponding cards.</li>
                        <li>Shuffle your Line Cards and place them face-down as a draw deck.</li>
                        <li>Place your Energy Card next to it with the number <b>3</b> facing up.</li>
                        <li>Finally, place your Start Card as shown in the setup diagram.</li>
                    </ul>
                </section>

                <section className="rules-section">
                    <h3 className="rules-section-title">START OF THE GAME</h3>
                    <ol className="rules-card-type-list">
                        <li>Each player reveals the top card from their deck.</li>
                        <li>Compare Initiative numbers — the player with the lower number goes first.</li>
                        <li>In case of a tie, reveal again until resolved.</li>
                    </ol>
                    <p>
                        Then, both players return revealed cards to their decks, shuffle, and draw <b>5 cards</b> for their starting hands.
                        <br />
                        <span className="rules-section-title-orange">
                            Note: If unhappy with your starting hand, you may redraw once — shuffle all 5 cards back and draw 5 new ones. You must keep the new hand.
                        </span>
                    </p>
                </section>

                <section className="rules-section">
                    <h3 className="rules-section-title">ROUND STRUCTURE</h3>
                    <ul className="rules-card-type-list">
                        <li>Compare the Initiative of the last Line Card placed by each player.</li>
                        <li>The lowest number plays first.</li>
                        <li>If tied, compare the previous card, and so on.</li>
                        <li>If you reach the Start Card, use the same turn order as Round 1.</li>
                    </ul>
                    <p>
                        Each round has <span className="rules-section-title-orange rules-card-type">3 phases</span>:
                    </p>
                    <ol className="rules-card-type-list">
                        <li>Turn Order Phase</li>
                        <li>Draw Phase</li>
                        <li>Action Phase</li>
                    </ol>
                </section>

                <section className="rules-section">
                    <h3 className="rules-section-title">DRAW PHASE</h3>
                    <p>
                        Each player draws from their deck until they have <b>5 cards</b> in hand.
                    </p>
                </section>

                <section className="rules-section">
                    <h3 className="rules-section-title">ACTION PHASE</h3>
                    <ul className="rules-card-type-list">
                        <li>Each card must connect properly (<span className="rules-section-title-orange">Entry → Exit</span>) with the last card played.</li>
                        <li>In Round 1: Each player places <b>1 Line Card</b> after their Start Card.</li>
                        <li>In later rounds: Each player must place <b>2 Line Cards</b> per round.</li>
                    </ul>
                    <p>
                        After both players have placed their cards, the round ends.
                    </p>
                </section>

                <section className="rules-section">
                    <h3 className="rules-section-title">ENERGY USE</h3>
                    <p>
                        You have <span className="rules-section-title-orange rules-card-type">3 Energy Points</span>, tracked with your Energy Card.
                        <br />
                        Energy can be spent starting from <b>Round 3</b> (max 1 point per round).
                        <br />
                        During your turn in the Action Phase, you can spend 1 Energy Point to use one of these effects:
                    </p>
                    <ul className="rules-card-type-list">
                        <li>
                            <b className="rules-section-title-orange">SPEED UP (ACELERÓN)</b>: Place <b>3 Line Cards</b> this round (instead of 2).
                        </li>
                        <li>
                            <b className="rules-section-title-orange">BRAKE (FRENAZO)</b>: Place <b>1 Line Card</b> this round (instead of 2).
                        </li>
                        <li>
                            <b className="rules-section-title-orange">REVERSE (MARCHA ATRÁS)</b>: Continue your line from a free Exit on your second-to-last Line Card (instead of the last).
                        </li>
                        <li>
                            <b className="rules-section-title-orange">EXTRA FUEL (GAS EXTRA)</b>: Draw <b>1 extra Line Card</b>.
                        </li>
                    </ul>
                    <p>
                        To activate an effect, rotate your Energy Card 90° clockwise to show it’s been used.
                        <br />
                        <span className="rules-section-title-orange">Manage your energy wisely — it’s limited!</span>
                    </p>
                </section>

                <section className="rules-section">
                    <h3 className="rules-section-title">END OF THE GAME</h3>
                    <p>
                        If a player cannot place any Line Card during their Action Phase, their line is cut and they lose.
                        <br />
                        <span className="rules-section-title-orange rules-card-type">Their opponent wins the game.</span>
                    </p>
                </section>

                <section className="rules-section">
                    <h3 className="rules-section-title-orange">ANNEX: PLAY AREA SIZES</h3>
                    <table className="rules-table">
                        <thead>
                            <tr className="rules-table-row">
                                <th className="rules-table-title">Players</th>
                                <th className="rules-table-title-orange">Play Area</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>2</td>
                                <td>7x7</td>
                            </tr>
                            <tr>
                                <td>3</td>
                                <td>7x7</td>
                            </tr>
                            <tr>
                                <td>4</td>
                                <td>9x9</td>
                            </tr>
                            <tr>
                                <td>5</td>
                                <td>9x9</td>
                            </tr>
                            <tr>
                                <td>6</td>
                                <td>11x11</td>
                            </tr>
                            <tr>
                                <td>7</td>
                                <td>11x11</td>
                            </tr>
                            <tr>
                                <td>8</td>
                                <td>13x13</td>
                            </tr>
                        </tbody>
                    </table>
                </section>
            </Container>
        </div>
    );
}
