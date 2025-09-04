import React from 'react';
import './preloader.css';

// types/index.ts
export interface LoaderProps {
    size?: 'small' | 'medium' | 'large';
    color?: string;
    text?: string;
    className?: string;
}

export interface LoadingButtonProps {
    loading: boolean;
    children: React.ReactNode;
    onClick?: () => void;
    disabled?: boolean;
    className?: string;
}

export interface ApiData {
    message: string;
    timestamp?: number;
}

// components/Loader.tsx

const Loader: React.FC<LoaderProps> = ({
    size = 'medium',
    color = '#007bff',
    text = 'Загрузка...',
    className = '',
}) => {
    return (
        <div className={`loader-container ${className}`}>
            <div className={`loader ${size}`} style={{ borderTopColor: color }}></div>
            {text && <p className="loader-text">{text}</p>}
        </div>
    );
};

export default Loader;
