import React from "react";
import { Container } from "reactstrap";
import "../../static/css/auth/authPage.css";
import bgImage from "../../static/images/home_background.png";

export default function RulesPage({ embed = false }) {
    const outerStyle = embed
        ? {
            background: "transparent",
            color: "white",
            minHeight: "auto",
            padding: "1.5rem 0",
            fontFamily: "Inter, Arial, sans-serif",
        }
        : {
            backgroundImage: `linear-gradient(rgba(0,0,0,0.2), rgba(0,0,0,0.2)), url(${bgImage})`,
            backgroundSize: "cover",
            backgroundPosition: "center",
            backgroundRepeat: "no-repeat",
            color: "white",
            minHeight: "100vh",
            padding: "2rem 0",
            fontFamily: "Inter, Arial, sans-serif",
        };

    return (
        <div style={outerStyle}>
            <Container
                className="auth-page-container"
                style={{
                    padding: "0 1rem",
                    maxWidth: 900,
                    background: "none",
                    borderRadius: embed ? "0.5rem" : "1rem",
                    boxShadow: embed ? "none" : "none",
                }}
            >
                <h1 className="text-center" style={{
                    fontWeight: 800,
                    letterSpacing: "2px",
                    marginBottom: "2rem",
                    color: "#FE5B02",
                    textShadow: "0 2px 8px #000"
                }}>
                    Rules and Guidelines
                </h1>
                <section style={{ marginBottom: "2rem" }}>
                    <p style={{ fontSize: "1.15rem", lineHeight: 1.7 }}>
                        <span style={{
                            background: "linear-gradient(90deg,#FE5B02,#B1D12D)",
                            WebkitBackgroundClip: "text",
                            WebkitTextFillColor: "transparent",
                            fontWeight: 700,
                            fontSize: "1.2rem"
                        }}>
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
                        <i style={{ color: "#888" }}>
                            *Each EOL box contains cards for 2 players. EOL allows up to 8 players if you have multiple copies of the game.
                        </i>
                    </p>
                </section>

                <section style={{ marginBottom: "2rem" }}>
                    <h3 style={{ color: "#B1D12D", fontWeight: 700, letterSpacing: "1px" }}>OBJECTIVE</h3>
                    <p style={{ fontSize: "1.1rem" }}>
                        In END OF LINE, your single goal is simple:
                        <br />
                        <span style={{
                            color: "#FE5B02",
                            fontWeight: 700,
                            fontSize: "1.15rem",
                            background: "rgba(254,91,2,0.08)",
                            padding: "0.2em 0.5em",
                            borderRadius: "0.3em"
                        }}>
                            Cut your opponent’s line before they cut yours.
                        </span>
                    </p>
                </section>

                <section style={{ marginBottom: "2rem" }}>
                    <h3 style={{ color: "#B1D12D", fontWeight: 700 }}>COMPONENTS <span style={{ color: "#FE5B02" }}>(54 CARDS)</span></h3>
                    <div style={{ textAlign: "center", margin: "1rem 0" }}>
                        <img
                            src={require('frontend/src/static/images/RulesIMG 1.png')}
                            alt="Rule illustration"
                            style={{
                                maxWidth: "80%",
                                height: "auto",
                                borderRadius: "0.5rem",
                                boxShadow: "0 2px 16px #222"
                            }}
                        />
                    </div>
                    <ul style={{ listStyle: "none", paddingLeft: 0, fontSize: "1.08rem" }}>
                        <li>- <span style={{ color: "#B1D12D" }}>2 Energy Cards</span></li>
                        <li>- <span style={{ color: "#B1D12D" }}>2 Start Cards</span></li>
                        <li>- <span style={{ color: "#B1D12D" }}>50 Line Cards</span></li>
                    </ul>
                </section>

                <section style={{ marginBottom: "2rem" }}>
                    <h3 style={{ color: "#B1D12D", fontWeight: 700 }}>PLAY AREA</h3>
                    <p>
                        In a 2-player VERSUS game, the play area is a <span style={{ color: "#B1D12D", fontWeight: 700 }}>7x7 grid</span> of Line Cards, whose edges are orthogonally connected.
                        <br />
                        <span style={{ color: "#B1D12D" }}>EOL does not use a physical board</span> — players build the grid as they place their Line Cards, without exceeding the 7x7 limit.
                        <br />
                        The side edges connect orthogonally (Top–Bottom; Left–Right).
                        <span style={{ color: "#FE5B02" }}> These boundaries are determined by the players’ placed Line Cards.</span>
                    </p>
                </section>

                <section style={{ marginBottom: "2rem" }}>
                    <h3 style={{ color: "#B1D12D", fontWeight: 700 }}>CARD TYPES</h3>
                    <div style={{ marginBottom: "1rem" }}>
                        <span style={{ color: "#B1D12D", fontWeight: 700 }}>START CARD</span>
                        <br />
                        <span style={{ color: "#ccc" }}>
                            This card begins your line. Place it on the table as shown in the setup section.
                        </span>
                    </div>
                    <div style={{ marginBottom: "1rem" }}>
                        <span style={{ color: "#B1D12D", fontWeight: 700 }}>ENERGY CARD</span>
                        <br />
                        <span style={{ color: "#ccc" }}>
                            Represents your energy. Its orientation shows how much you have left.
                            You start with <span style={{ color: "#FE5B02", fontWeight: 700 }}>3 Energy Points</span>.
                        </span>
                    </div>
                    <div>
                        <span style={{ color: "#B1D12D", fontWeight: 700 }}>LINE CARDS</span>
                        <br />
                        <span style={{ color: "#ccc" }}>
                            Used to form your path or line. There are <b>8 types</b> of Line Cards.
                            Each Line Card is divided into 3 sections:
                        </span>
                        <ul style={{ marginLeft: "1.5em" }}>
                            <li>
                                <span style={{ color: "#FE5B02", fontWeight: 700 }}>Entry</span> – where your line comes in.
                            </li>
                            <li>
                                <span style={{ color: "#FE5B02", fontWeight: 700 }}>Exit(s)</span> – arrow(s) showing where your line continues.
                            </li>
                            <li>
                                <span style={{ color: "#FE5B02", fontWeight: 700 }}>Initiative Number</span> – determines turn order each round.
                            </li>
                        </ul>
                        <span style={{ color: "#888" }}>
                            Note: When placing a Line Card, ensure its Entry arrow matches one of the Exit arrows from the previous card. Each Line Card has only 1 Entry but may have multiple Exits.
                        </span>
                    </div>
                </section>

                <section style={{ marginBottom: "2rem" }}>
                    <h3 style={{ color: "#B1D12D", fontWeight: 700 }}>SETUP</h3>
                    <ul style={{ marginLeft: "1.5em" }}>
                        <li>Choose a color and take all corresponding cards.</li>
                        <li>Shuffle your Line Cards and place them face-down as a draw deck.</li>
                        <li>Place your Energy Card next to it with the number <b>3</b> facing up.</li>
                        <li>Finally, place your Start Card as shown in the setup diagram.</li>
                    </ul>
                </section>

                <section style={{ marginBottom: "2rem" }}>
                    <h3 style={{ color: "#B1D12D", fontWeight: 700 }}>START OF THE GAME</h3>
                    <ol style={{ marginLeft: "1.5em" }}>
                        <li>Each player reveals the top card from their deck.</li>
                        <li>Compare Initiative numbers — the player with the lower number goes first.</li>
                        <li>In case of a tie, reveal again until resolved.</li>
                    </ol>
                    <p>
                        Then, both players return revealed cards to their decks, shuffle, and draw <b>5 cards</b> for their starting hands.
                        <br />
                        <span style={{ color: "#FE5B02" }}>
                            Note: If unhappy with your starting hand, you may redraw once — shuffle all 5 cards back and draw 5 new ones. You must keep the new hand.
                        </span>
                    </p>
                </section>

                <section style={{ marginBottom: "2rem" }}>
                    <h3 style={{ color: "#B1D12D", fontWeight: 700 }}>ROUND STRUCTURE</h3>
                    <ul style={{ marginLeft: "1.5em" }}>
                        <li>Compare the Initiative of the last Line Card placed by each player.</li>
                        <li>The lowest number plays first.</li>
                        <li>If tied, compare the previous card, and so on.</li>
                        <li>If you reach the Start Card, use the same turn order as Round 1.</li>
                    </ul>
                    <p>
                        Each round has <span style={{ color: "#FE5B02", fontWeight: 700 }}>3 phases</span>:
                    </p>
                    <ol style={{ marginLeft: "1.5em" }}>
                        <li>Turn Order Phase</li>
                        <li>Draw Phase</li>
                        <li>Action Phase</li>
                    </ol>
                </section>

                <section style={{ marginBottom: "2rem" }}>
                    <h3 style={{ color: "#B1D12D", fontWeight: 700 }}>DRAW PHASE</h3>
                    <p>
                        Each player draws from their deck until they have <b>5 cards</b> in hand.
                    </p>
                </section>

                <section style={{ marginBottom: "2rem" }}>
                    <h3 style={{ color: "#B1D12D", fontWeight: 700 }}>ACTION PHASE</h3>
                    <ul style={{ marginLeft: "1.5em" }}>
                        <li>Each card must connect properly (<span style={{ color: "#FE5B02" }}>Entry → Exit</span>) with the last card played.</li>
                        <li>In Round 1: Each player places <b>1 Line Card</b> after their Start Card.</li>
                        <li>In later rounds: Each player must place <b>2 Line Cards</b> per round.</li>
                    </ul>
                    <p>
                        After both players have placed their cards, the round ends.
                    </p>
                </section>

                <section style={{ marginBottom: "2rem" }}>
                    <h3 style={{ color: "#B1D12D", fontWeight: 700 }}>ENERGY USE</h3>
                    <p>
                        You have <span style={{ color: "#FE5B02", fontWeight: 700 }}>3 Energy Points</span>, tracked with your Energy Card.
                        <br />
                        Energy can be spent starting from <b>Round 3</b> (max 1 point per round).
                        <br />
                        During your turn in the Action Phase, you can spend 1 Energy Point to use one of these effects:
                    </p>
                    <ul style={{ marginLeft: "1.5em" }}>
                        <li>
                            <b style={{ color: "#FE5B02" }}>SPEED UP (ACELERÓN)</b>: Place <b>3 Line Cards</b> this round (instead of 2).
                        </li>
                        <li>
                            <b style={{ color: "#FE5B02" }}>BRAKE (FRENAZO)</b>: Place <b>1 Line Card</b> this round (instead of 2).
                        </li>
                        <li>
                            <b style={{ color: "#FE5B02" }}>REVERSE (MARCHA ATRÁS)</b>: Continue your line from a free Exit on your second-to-last Line Card (instead of the last).
                        </li>
                        <li>
                            <b style={{ color: "#FE5B02" }}>EXTRA FUEL (GAS EXTRA)</b>: Draw <b>1 extra Line Card</b>.
                        </li>
                    </ul>
                    <p>
                        To activate an effect, rotate your Energy Card 90° clockwise to show it’s been used.
                        <br />
                        <span style={{ color: "#FE5B02" }}>Manage your energy wisely — it’s limited!</span>
                    </p>
                </section>

                <section style={{ marginBottom: "2rem" }}>
                    <h3 style={{ color: "#B1D12D", fontWeight: 700 }}>END OF THE GAME</h3>
                    <p>
                        If a player cannot place any Line Card during their Action Phase, their line is cut and they lose.
                        <br />
                        <span style={{ color: "#FE5B02", fontWeight: 700 }}>Their opponent wins the game.</span>
                    </p>
                </section>

                <section style={{ marginBottom: "2rem" }}>
                    <h3 style={{ color: "#FE5B02", fontWeight: 700 }}>ANNEX: PLAY AREA SIZES</h3>
                    <table style={{
                        width: "100%",
                        background: "#222",
                        borderRadius: "0.5em",
                        borderCollapse: "collapse",
                        marginTop: "1em",
                        color: "#fff",
                        fontSize: "1.05rem"
                    }}>
                        <thead>
                            <tr style={{ background: "#333" }}>
                                <th style={{ padding: "0.5em", color: "#B1D12D", fontWeight: 700 }}>Players</th>
                                <th style={{ padding: "0.5em", color: "#FE5B02", fontWeight: 700 }}>Play Area</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td style={{ padding: "0.5em" }}>2</td>
                                <td style={{ padding: "0.5em" }}>7x7</td>
                            </tr>
                            <tr>
                                <td style={{ padding: "0.5em" }}>3</td>
                                <td style={{ padding: "0.5em" }}>7x7</td>
                            </tr>
                            <tr>
                                <td style={{ padding: "0.5em" }}>4</td>
                                <td style={{ padding: "0.5em" }}>9x9</td>
                            </tr>
                            <tr>
                                <td style={{ padding: "0.5em" }}>5</td>
                                <td style={{ padding: "0.5em" }}>9x9</td>
                            </tr>
                            <tr>
                                <td style={{ padding: "0.5em" }}>6</td>
                                <td style={{ padding: "0.5em" }}>11x11</td>
                            </tr>
                            <tr>
                                <td style={{ padding: "0.5em" }}>7</td>
                                <td style={{ padding: "0.5em" }}>11x11</td>
                            </tr>
                            <tr>
                                <td style={{ padding: "0.5em" }}>8</td>
                                <td style={{ padding: "0.5em" }}>13x13</td>
                            </tr>
                        </tbody>
                    </table>
                </section>
            </Container>
        </div>
    );
}
