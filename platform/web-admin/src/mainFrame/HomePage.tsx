import { initStateSetters } from '@/common/component';
import { HomePageSkeleton } from '@g/mainFrame/HomePageSkeleton';

function HomePageFC(props: { element: HomePageComponent }) {
    initStateSetters(props.element);
    return <div>Home</div>;
}

export class HomePageComponent extends HomePageSkeleton {
    functionalComponent = HomePageFC;
    getProcessedChildren(): string[] {
        return [];
    }
}
