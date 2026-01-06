import React, { useState, useEffect } from 'react';
import { Navbar, NavbarBrand, NavLink, NavItem, Nav, UncontrolledDropdown, DropdownToggle, DropdownMenu, DropdownItem, NavbarToggler, Collapse } from 'reactstrap';
import { Link, useLocation } from 'react-router-dom';
import tokenService from './services/token.service';
import jwt_decode from "jwt-decode";

function AppNavbar() {
    const [dropdownOpen, setDropdownOpen] = useState(false);
    const [roles, setRoles] = useState([]);
    const [username, setUsername] = useState("");
    const jwt = tokenService.getLocalAccessToken();
    const [collapsed, setCollapsed] = useState(true);

    const toggleDropdown = (state) => setDropdownOpen(state);
    const toggleNavbar = () => setCollapsed(!collapsed);

    const location = useLocation();
    console.log(location.pathname);
    const isInGame = location.pathname.startsWith('/game/');
    const isInLobby = location.pathname.startsWith('/lobby/');

    useEffect(() => {
        if (jwt) {
            setRoles(jwt_decode(jwt).authorities);
            setUsername(jwt_decode(jwt).sub);
        }
    }, [jwt])

    let adminLinks = <></>;
    let ownerLinks = <></>;
    let userLinks = <></>;
    let publicLinks = <></>;
    let playerLinks = <></>;
    let gameLinks = <></>;

    if (!jwt) {
        publicLinks = (
            <>
                <NavItem>
                    <NavLink style={{ color: "white" }} id="register" tag={Link} to="/register">Register</NavLink>
                </NavItem>
                <NavItem>
                    <NavLink style={{ color: "white" }} id="login" tag={Link} to="/login">Login</NavLink>
                </NavItem>
            </>
        )
    } else {
        userLinks = (
            <>
            </>
        )
    
    roles.forEach((role) => {
        if (role === "ADMIN") {
            if (!isInGame) {
                adminLinks = (   
                    <>       
                        <NavItem>
                            <NavLink style={{ color: "white" }} id="rules" tag={Link} to="/rules">Rules</NavLink>
                        </NavItem>        
                        <UncontrolledDropdown
                        nav
                        inNavbar
                        onMouseEnter={() => toggleDropdown(true)}
                        onMouseLeave={() => toggleDropdown(false)}
                        isOpen={dropdownOpen}
                        style={{ marginLeft: "auto" }}
                    >
                        <DropdownToggle nav caret style={{ color: "white" }}>
                        {username}
                        </DropdownToggle>

                        <DropdownMenu 
                            end
                            dark
                            style={{ backgroundColor: "#1e1e1e", minWidth: "200px" }}>
                        <DropdownItem tag={Link} 
                                to="/myprofile"
                                style={{
                                    color: "#b1d12d",
                                    fontWeight: "500",
                                }}>
                            Profile
                        </DropdownItem>
                        <DropdownItem tag={Link} 
                                    to="/games"
                                    style={{
                                        color: "#FE5B02",
                                        fontWeight: "500",
                                    }}>
                            Games
                        </DropdownItem>
                        <DropdownItem tag={Link} 
                                    to="/users"
                                    style={{
                                        color: "#b1d12d",
                                        fontWeight: "500",
                                    }}>
                            Players
                        </DropdownItem>
                        <DropdownItem tag={Link} 
                                    to="/developers"
                                    style={{
                                        color: "#FE5B02",
                                        fontWeight: "500",
                                    }}>
                            Developers
                        </DropdownItem>
                        <DropdownItem tag={Link} 
                                    to="/social"
                                    style={{
                                        color: "#b1d12d",
                                        fontWeight: "500",
                                    }}>
                            Social
                        </DropdownItem>
                        <DropdownItem tag={Link} 
                                    to="/achievements"
                                    style={{
                                        color: "#FE5B02",
                                        fontWeight: "500",
                                    }}>
                            Achievements
                        </DropdownItem>
                    <DropdownItem divider />
                    <DropdownItem tag={Link} to="/logout">
                        Logout
                    </DropdownItem>
                    </DropdownMenu>
                </UncontrolledDropdown>
                </>
            )
            }
        }
        if (role === "PLAYER") {
            if (isInGame || isInLobby) {
                gameLinks = (isInGame || isInLobby) && (
                <>
                </>
                )
            }
            else {
                playerLinks = (
                        <>
                            <NavItem>
                            <NavLink style={{ color: "white" }} id="creategame" tag={Link} to="/creategame">Play now!</NavLink>
                            </NavItem>
                            <NavItem>
                            <NavLink style={{ color: "white" }} id="rules" tag={Link} to="/rules">Rules</NavLink>
                            </NavItem>
                            <UncontrolledDropdown
                            nav
                            inNavbar
                            onMouseEnter={() => toggleDropdown(true)}
                            onMouseLeave={() => toggleDropdown(false)}
                            isOpen={dropdownOpen}
                            style={{ marginLeft: "auto" }}
                        >
                            <DropdownToggle nav caret style={{ color: "white" }}>
                            {username}
                            </DropdownToggle>

                            <DropdownMenu 
                                end
                                dark
                                style={{ backgroundColor: "#1e1e1e", minWidth: "200px" }}>
                            <DropdownItem tag={Link} 
                                    to="/myprofile"
                                    style={{
                                        color: "#b1d12d",
                                        fontWeight: "500",
                                    }}>
                                Profile
                            </DropdownItem>
                            <DropdownItem tag={Link} 
                                        to="/friends"
                                        style={{
                                            color: "#FE5B02",
                                            fontWeight: "500",
                                        }}>
                                Friends
                            </DropdownItem>
                            <DropdownItem tag={Link} 
                                        to="/games"
                                        style={{
                                            color: "#b1d12d",
                                            fontWeight: "500",
                                        }}>
                                Games
                            </DropdownItem>
                            <DropdownItem tag={Link} 
                                        to="/achievements"
                                        style={{
                                            color: "#FE5B02",
                                            fontWeight: "500",
                                        }}>
                                Achievements
                            </DropdownItem>
                            <DropdownItem tag={Link} 
                                        to="/stats"
                                        style={{
                                            color: "#b1d12d",
                                            fontWeight: "500",
                                        }}>
                                Stats
                            </DropdownItem>
                            <DropdownItem tag={Link} 
                                        to="/social"
                                        style={{
                                            color: "#FE5B02",
                                            fontWeight: "500",
                                        }}>
                                Social
                            </DropdownItem>
                            <DropdownItem divider />
                            <DropdownItem tag={Link} to="/logout">
                                Logout
                            </DropdownItem>
                            </DropdownMenu>
                        </UncontrolledDropdown>
                        </>
            )
            }
        } 
    })
}

    return (
        <div>
            <Navbar expand="md" dark color="black">
                <NavbarBrand href={isInGame || isInLobby ? null : "/"}>
                    <img
                        alt="logo"
                        src='/images/logo_small.png'
                        style={{
                            height: 40,
                            width: 40,
                            marginRight: 10
                        }}
                    />
                    <span
                        style={{
                            color: "#b1d12d",
                            fontWeight: "bold"
                        }}
                    >
                        ENDOF
                    </span>
                    <span
                        style={{
                            color: "#ff5c00",
                            fontWeight: "bold"
                        }}
                    >
                        LINE
                    </span>
                </NavbarBrand>
                <NavbarToggler onClick={toggleNavbar} className="ms-2" />
                <Collapse isOpen={!collapsed} navbar>
                    <Nav className="me-auto mb-2 mb-lg-0" navbar>
                        {gameLinks}
                        {userLinks}
                        {ownerLinks}
                    </Nav>
                    <Nav className="ms-auto mb-2 mb-lg-0" navbar>
                        {adminLinks}
                        {publicLinks}
                        {playerLinks}
                    </Nav>
                </Collapse>
            </Navbar>
        </div>
    );
}

export default AppNavbar;