export function fmtHours(mins) {
  return (mins / 60).toFixed(1);
}

export function fmtMins(n) {
  return Math.round(n);
}

export function fmtSkill(s) {
  if (!s) return '—';
  try {
    return String(s)
      .replace(/_/g, ' ')
      .toLowerCase()
      .replace(/\b\w/g, (c) => c.toUpperCase());
  } catch {
    return s;
  }
}
