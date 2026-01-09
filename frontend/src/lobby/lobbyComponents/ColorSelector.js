export default function ColorSelector({ colorImages, selectedColor, onSelect, disabled }) {
  const style = disabled
    ? { pointerEvents: 'none', opacity: 0.4, backgroundColor: '#1C1C1C', borderRadius: '10px' }
    : { pointerEvents: 'auto', opacity: 1 };

  return (
    <div className="colors" style={style}>
      {colorImages.map((choice) => (
        <div
          key={choice.color}
          className={`color-image-container ${selectedColor === choice.color ? "selected-color-container" : ""}`}
        >
          <img
            src={choice.image}
            alt={choice.label}
            className="color-image"
            onClick={() => onSelect(choice.color)}
            style={{ width: "100%", height: "auto" }}
          />
        </div>
      ))}
    </div>
  );
}
