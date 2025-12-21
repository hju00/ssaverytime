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
  
  // MASTER 처리 (31)
  if (tierStr.toUpperCase() === 'MASTER') return 31;

  const tiers = ['BRONZE', 'SILVER', 'GOLD', 'PLATINUM', 'DIAMOND', 'RUBY'];
  
  const parts = tierStr.split(' ');
  if (parts.length < 2) return 0;
  
  const tierIndex = tiers.indexOf(parts[0]);
  if (tierIndex === -1) return 0;
  
  const levelNum = {'V': 1, 'IV': 2, 'III': 3, 'II': 4, 'I': 5}[parts[1]] || 0;
  return (tierIndex * 5) + levelNum;
}

export const getTierImageSrc = (tier) => {
  if (!tier) return null;
  
  // 이미 URL인 경우 (새로운 방식)
  if (tier.startsWith('http') || tier.includes('.svg')) {
    return tier;
  }

  // 기존 문자열 형식인 경우 ("GOLD V") -> 숫자 변환 -> URL 생성
  const num = getTierNumber(tier);
  if (num > 0) {
    return `https://static.solved.ac/tier_small/${num}.svg`;
  }
  
  return null;
}

export const getTierName = (tierVal) => {
  if (!tierVal) return 'Unranked';

  let num = 0;
  // URL에서 숫자 추출
  if (typeof tierVal === 'string' && tierVal.includes('solved.ac')) {
    const match = tierVal.match(/(\d+)\.svg/);
    if (match) num = parseInt(match[1]);
  } else if (typeof tierVal === 'number') {
    num = tierVal;
  } else {
    // 이미 문자열(GOLD V)인 경우 그대로 반환하거나 변환 로직 추가 가능
    return tierVal;
  }

  if (!num || num <= 0) return 'Unranked';
  if (num === 31) return 'Master';

  const tiers = ['Bronze', 'Silver', 'Gold', 'Platinum', 'Diamond', 'Ruby'];
  const tierIdx = Math.floor((num - 1) / 5);
  
  // 0 -> V, 1 -> IV, 2 -> III, 3 -> II, 4 -> I
  const subLevelIdx = (num - 1) % 5;
  const subTiers = ['V', 'IV', 'III', 'II', 'I'];

  if (tierIdx < 0 || tierIdx >= tiers.length) return 'Unknown';

  return `${tiers[tierIdx]} ${subTiers[subLevelIdx]}`;
}
