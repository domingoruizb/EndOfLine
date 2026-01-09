export default function GameCodeInput({ code, setCode }) {
  return (
    <div style={{ margin: '20px 0', width: '100%' }}>
      <label htmlFor="gameCode" style={{ color: 'white', display: 'block', marginBottom: '10px', fontSize: '1.2em' }}>
        Enter Game Code:
      </label>
      <input
        type="text"
        id="gameCode"
        name="gameCode"
        value={code}
        onChange={(e) => setCode(e.target.value)}
        maxLength={6}
        required
        placeholder="A1B2C3"
        style={{ 
          padding: '10px', 
          fontSize: '1.5em',
          textAlign: 'center',
          textTransform: 'uppercase',
          width: '80%', 
          maxWidth: '300px',
          borderRadius: '5px',
          backgroundColor: '#2C2C2C',
          color: 'white',
          border: '2px solid #b1d12d'
        }}
      />
    </div>
  );
}
