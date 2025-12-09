import { clsx } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs) {
  return twMerge(clsx(inputs));
}

export function valueUpdater(updaterOrValue, ref) {
  ref.value =
    typeof updaterOrValue === "function"
      ? updaterOrValue(ref.value)
      : updaterOrValue;
}

export const getTierNumber = (tierStr) => {
  if (!tierStr) return 0;
  const tiers = ['BRONZE', 'SILVER', 'GOLD', 'PLATINUM', 'DIAMOND', 'RUBY', 'MASTER'];
  
  const parts = tierStr.split(' ');
  if (parts.length < 2) return 0;
  
  const tierIndex = tiers.indexOf(parts[0]);
  if (tierIndex === -1) return 0;
  
  const levelNum = {'V': 1, 'IV': 2, 'III': 3, 'II': 4, 'I': 5}[parts[1]] || 0;
  return (tierIndex * 5) + levelNum;
}
